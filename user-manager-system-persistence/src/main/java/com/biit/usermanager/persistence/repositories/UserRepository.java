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

    Optional<User> findByUsernameHash(String username);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByUuid(UUID uuid);

    List<User> findByUuidIn(Collection<UUID> uuid);

    List<User> findAllByAccountBlocked(boolean accountBlocked);

    List<User> findByAccountExpired(boolean accountExpired);

    long deleteByUsernameHash(String username);
}
