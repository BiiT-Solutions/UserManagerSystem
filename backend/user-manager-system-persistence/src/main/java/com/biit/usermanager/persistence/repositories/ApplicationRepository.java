package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Application;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ApplicationRepository extends ElementRepository<Application, Long> {
    Optional<Application> findByName(String name);
}
