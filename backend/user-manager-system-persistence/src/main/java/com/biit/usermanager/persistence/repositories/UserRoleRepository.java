package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Group;
import com.biit.usermanager.persistence.entities.Role;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.entities.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface UserRoleRepository extends ElementRepository<UserRole, Long> {

    List<UserRole> findByUserAndGroup(User user, Group group);

    List<UserRole> findByUserAndGroupIn(User user, Collection<Group> group);

    List<UserRole> findByUser(User user);

    List<UserRole> findByGroupAndRole(Group group, Role role);

    List<UserRole> findByGroup(Group group);

    List<UserRole> findByRole(Role role);
}
