package com.biit.usermanager.core.providers;


import com.biit.server.exceptions.InvalidPageSizeException;
import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.core.exceptions.ExternalReferenceAlreadyExistsException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.PasswordResetTokenRepository;
import com.biit.usermanager.persistence.repositories.TeamMemberRepository;
import com.biit.usermanager.persistence.repositories.UserApplicationBackendServiceRoleRepository;
import com.biit.usermanager.persistence.repositories.UserGroupUserRepository;
import com.biit.usermanager.persistence.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

@Service
public class UserProvider extends ElementProvider<User, Long, UserRepository> {

    private final UserGroupUserRepository userGroupUserRepository;

    private final TeamMemberRepository teamMemberRepository;

    private final UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserProvider(UserRepository repository, UserGroupUserRepository userGroupUserRepository,
                        TeamMemberRepository teamMemberRepository,
                        UserApplicationBackendServiceRoleRepository userApplicationBackendServiceRoleRepository,
                        PasswordResetTokenRepository passwordResetTokenRepository) {
        super(repository);
        this.userGroupUserRepository = userGroupUserRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userApplicationBackendServiceRoleRepository = userApplicationBackendServiceRoleRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }


    @Override
    public User save(User entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getId() == null) {
            return getRepository().save(entity);
        } else {
            //Is it an update?
            //Do not update the password field!
            final User databaseUser = getRepository().findById(entity.getId()).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                    "No User with id '" + entity.getId() + "' found on the system."));
            databaseUser.copy(entity);
            UserManagerLogger.debug(this.getClass(), "Updating user '{}'.", databaseUser.getUsername());
            return getRepository().save(databaseUser);
        }
    }

    public Optional<User> findByUsername(String username) {
        //If encryption is enabled, use hash.
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            final Optional<User> authenticatedUser = findByUsernameHash(username);
            if (authenticatedUser.isPresent()) {
                authenticatedUser.get().setUsernameHash(authenticatedUser.get().getUsername().toLowerCase());
                return authenticatedUser;
            }
            return Optional.empty();
        } else {
            return getRepository().findByUsernameIgnoreCase(username);
        }
    }


    public List<User> findByUsernames(Collection<String> usernames) {
        //If encryption is enabled, use hash.
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            final List<User> authenticatedUser = findByUsernameHash(usernames);
            authenticatedUser.forEach(u -> u.setUsernameHash(u.getUsername().toLowerCase()));
            return authenticatedUser;
        } else {
            return getRepository().findByUsernameIn(usernames);
        }
    }


    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }

        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            final List<User> authenticatedUser = findByEmailHash(email);
            //We have some legacy data where an email is shared between multiple email accounts.
            if (!authenticatedUser.isEmpty()) {
                authenticatedUser.get(0).setEmail(authenticatedUser.get(0).getEmail().toLowerCase().trim());
                return Optional.of(authenticatedUser.get(0));
            }
            return Optional.empty();
        } else {
            final List<User> users = getRepository().findByEmailIgnoreCase(email.trim());
            if (users.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(users.get(0));
        }
    }


    public Optional<User> getById(String uuid) {
        try {
            return getById(Long.parseLong(uuid));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByUsernameHash(String username) {
        return getRepository().findByUsernameHash(username.toLowerCase());
    }

    public List<User> findByUsernameHash(Collection<String> usernames) {
        return getRepository().findByUsernameHashIn(usernames);
    }

    public List<User> findByEmailHash(String email) {
        return getRepository().findByEmailHash(email);
    }

    public List<User> findByEmailHash(Collection<String> emails) {
        return getRepository().findByEmailHashIn(emails);
    }

    public Optional<User> findByUuid(UUID uuid) {
        return getRepository().findByUuid(uuid);
    }

    public List<User> findByUuids(Collection<UUID> uuids) {
        return getRepository().findByUuidIn(uuids);
    }

    public List<User> findByUuids(Collection<UUID> uuids, Collection<String> createdOn) {
        return getRepository().findByUuidInAndCreatedOnHashIn(uuids, createdOn);
    }

    public Optional<User> getById(Long id) {
        return getRepository().findById(id);
    }


    public List<User> findAllByAccountBlocked(boolean accountBlocked) {
        return getRepository().findAllByAccountBlocked(accountBlocked);
    }

    public List<User> getByUserGroup(Long userGroupId) {
        return findByIdIn(userGroupUserRepository.findByIdUserGroupId(userGroupId).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserId()).toList());
    }

    public List<User> getByTeam(Long teamId) {
        return getByTeam(teamId, 0, DEFAULT_PAGE_SIZE);
    }

    public List<User> getByTeam(Long teamId, int page, int size) {
        if (size > MAX_PAGE_SIZE) {
            throw new InvalidPageSizeException(this.getClass(), "Page size is too large. Máx allowed page size is '"
                    + MAX_PAGE_SIZE + "'.");
        }
        final Pageable pageable = PageRequest.of(page, size);
        return findByIdIn(teamMemberRepository.findByIdTeamId(teamId, pageable).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserId()).toList());
    }

    @Override
    @Transactional
    public void delete(User entity) {
        if (entity != null) {
            //Delete all FK first.
            teamMemberRepository.deleteAll(teamMemberRepository.findByIdUserId(entity.getId()));
            userGroupUserRepository.deleteAll(userGroupUserRepository.findByIdUserId(entity.getId()));
            passwordResetTokenRepository.deleteByUser(entity);
            //Flush is needed to avoid a ObjectOptimisticLockingFailureException: Row was updated or deleted by another
            //transaction (or unsaved-value mapping was incorrect).
            userApplicationBackendServiceRoleRepository.deleteByIdUserId(entity.getId());
            userApplicationBackendServiceRoleRepository.flush();
            //Delete entity.
            getRepository().delete(entity);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void deleteAll() {
        deleteAll(findAll());
    }

    @Override
    @Transactional
    public void deleteAll(Collection<User> entities) {
        entities.forEach(this::delete);
    }


    public Optional<User> findByExternalReference(String externalReference) {
        if (externalReference == null) {
            return Optional.empty();
        }
        final List<User> users = getRepository().findByExternalReference(externalReference);
        //As external reference is not unique (can have nulls), we can have a list. But only one is expected.
        if (users.isEmpty()) {
            return Optional.empty();
        }
        if (users.size() > 1) {
            throw new ExternalReferenceAlreadyExistsException(this.getClass(), "Multiple users found for external reference '" + externalReference + "'");
        }
        return Optional.of(users.get(0));
    }

    public List<User> findByExternalReferences(List<String> externalReferences) {
        if (externalReferences == null || externalReferences.isEmpty()) {
            return new ArrayList<>();
        }
        return getRepository().findByExternalReferenceIn(externalReferences);
    }

}
