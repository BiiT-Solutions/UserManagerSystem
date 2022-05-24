package com.biit.usermanager.persistence.repositories;

import com.biit.usermanager.persistence.entities.Organization;
import com.biit.usermanager.persistence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
