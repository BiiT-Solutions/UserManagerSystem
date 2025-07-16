package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.Organization;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface OrganizationRepository extends ElementRepository<Organization, String> {

    Optional<Organization> findByNameIgnoreCase(String name);

    @Query("""
            SELECT o FROM Organization o WHERE o.name=
            (SELECT t.organization.name FROM Team t WHERE t.id=:teamId)
            """)
    Optional<Organization> findByTeam(Long teamId);

    @Query("""
            SELECT DISTINCT o, tm.createdAt FROM Organization o
                        RIGHT JOIN Team t ON t.organization.id=o.id
                                    RIGHT JOIN TeamMember tm ON t.id=tm.id.teamId WHERE tm.id.userId=:userId
                                                ORDER BY tm.createdAt DESC
            """)
    Set<Organization> findByUser(Long userId);
}
