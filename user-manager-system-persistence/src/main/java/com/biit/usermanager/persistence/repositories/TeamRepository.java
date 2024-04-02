package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Team;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface TeamRepository extends ElementRepository<Team, Long> {

    Optional<Team> findByNameAndOrganization(String name, Organization organization);

    int deleteByNameAndOrganization(String name, Organization organization);

    List<Team> findByParent(Team parent);

    List<Team> findByParentIsNull();

    List<Team> findByParentIsNotNull();

    List<Team> findByOrganization(Organization organization);


}
