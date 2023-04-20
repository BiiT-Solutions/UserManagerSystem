package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    int deleteByName(String name);

    List<Group> findByParentIsNull();

    List<Group> findByParentIsNotNull();


}
