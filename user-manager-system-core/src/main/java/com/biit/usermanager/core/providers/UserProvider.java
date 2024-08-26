package com.biit.usermanager.core.providers;


import com.biit.server.providers.ElementProvider;
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
import org.springframework.stereotype.Service;

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
            UserManagerLogger.debug(this.getClass(), "Updating user '{}' with password '{}'.", entity.getUsername(), entity.getPassword());
            //Is it an update?
            //Do not update the password field!
            final User databaseUser = getRepository().findById(entity.getId()).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                    "No User with id '" + entity.getId() + "' found on the system."));
            databaseUser.copy(entity);
            UserManagerLogger.debug(this.getClass(), "Updating databaseUser '{}' with password '{}'.", databaseUser.getUsername(), databaseUser.getPassword());
            return getRepository().save(databaseUser);
        }
    }

    public Optional<User> findByUsername(String username) {
        //If encryption is enabled, use hash.
        if (getEncryptionKey() != null) {
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

    public Optional<User> findByEmail(String email) {
        return getRepository().findByEmailIgnoreCase(email);
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

    public Optional<User> findByUuid(UUID uuid) {
        return getRepository().findByUuid(uuid);
    }

    public List<User> findByUuids(Collection<UUID> uuids) {
        return getRepository().findByUuidIn(uuids);
    }

    public Optional<User> getById(Long id) {
        return getRepository().findById(id);
    }


    public List<User> findAllByAccountBlocked(boolean accountBlocked) {
        return getRepository().findAllByAccountBlocked(accountBlocked);
    }

    public List<User> findByAccountExpired(boolean accountExpired) {
        return getRepository().findByAccountExpired(accountExpired);
    }

    @Override
    public List<User> findAll() {
        return getRepository().findAll();
    }

    public List<User> getByUserGroup(Long userGroupId) {
        return findByIdIn(userGroupUserRepository.findByIdUserGroupId(userGroupId).stream()
                .map(userGroupUsers -> userGroupUsers.getId().getUserId()).toList());
    }

    public List<User> getByTeam(Long teamId) {
        return findByIdIn(teamMemberRepository.findByIdTeamId(teamId).stream()
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

}
