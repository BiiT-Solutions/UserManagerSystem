package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByName(String name);
}
