package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends ElementRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

    Optional<User> findByUsernameHash(String username);

    List<User> findByUsernameHashIn(Collection<String> usernames);

    List<User> findByEmailHash(String email);

    List<User> findByEmailHashIn(Collection<String> emails);

    List<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUuid(UUID uuid);

    List<User> findByUuidIn(Collection<UUID> uuid);

    List<User> findByUuidInAndCreatedOnHashIn(Collection<UUID> uuid, Collection<String> createdOn);

    List<User> findAllByAccountBlocked(boolean accountBlocked);

    long deleteByUsernameHash(String username);

    List<User> findByExternalReference(String externalReference);

    List<User> findByExternalReferenceIn(Collection<String> externalReference);
}
