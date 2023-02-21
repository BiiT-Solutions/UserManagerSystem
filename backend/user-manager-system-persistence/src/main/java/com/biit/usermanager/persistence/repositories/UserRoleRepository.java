package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserAndOrganization(User user, Organization organization);

    List<UserRole> findByUserAndOrganizationAndApplication(User user, Organization organization, Application application);

    List<UserRole> findByUserAndApplication(User user, Application application);

    UserRole findByUser(User user);

    List<UserRole> findByRole(Role role);
}
