package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Group;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface GroupRepository extends ElementRepository<Group, Long> {

    Optional<Group> findByName(String name);

    int deleteByName(String name);

    List<Group> findByParentIsNull();

    List<Group> findByParentIsNotNull();


}
