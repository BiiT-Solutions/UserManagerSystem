package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface GroupRepository extends ElementRepository<Group, Long> {

    Optional<Group> findByNameAndApplication(String name, Application application);

    int deleteByNameAndApplication(String name, Application application);

    List<Group> findByParentIsNull();

    List<Group> findByParentIsNotNull();

    List<Group> findByApplication(Application application);


}
