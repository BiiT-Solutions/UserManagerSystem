package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Team;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface TeamRepository extends ElementRepository<Team, Long> {

    Optional<Team> findByNameAndApplication(String name, Application application);

    int deleteByNameAndApplication(String name, Application application);

    List<Team> findByParentIsNull();

    List<Team> findByParentIsNotNull();

    List<Team> findByApplication(Application application);


}
