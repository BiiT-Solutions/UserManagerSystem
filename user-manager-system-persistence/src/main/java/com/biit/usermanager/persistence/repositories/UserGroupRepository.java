package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.UserGroup;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserGroupRepository extends ElementRepository<UserGroup, Long> {

    Optional<UserGroup> findByNameIgnoreCase(String name);

    long deleteByNameIgnoreCase(String name);
}
