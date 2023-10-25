package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.BackendService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface BackendServiceRepository extends ElementRepository<BackendService, Long> {
    Optional<BackendService> findByName(String name);
}
