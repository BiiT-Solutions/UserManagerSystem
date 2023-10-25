package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@Transactional
public interface RoleRepository extends ElementRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
