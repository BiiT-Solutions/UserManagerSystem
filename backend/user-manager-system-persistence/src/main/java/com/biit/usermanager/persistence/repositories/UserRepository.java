package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdCard(String idCard);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByNameAndLastname(String name, String lastname);

    List<User> findAllByAccountBlocked(boolean accountBlocked);

    Optional<User> findByPhone(String phone);

    List<User> findAllByPhone(String phone);

    List<User> findByAccountExpired(boolean accountExpired);

    Optional<User> deleteByUsername(String username);
}
