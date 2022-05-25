package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserAndOrganization(User user, Organization organization);
}
