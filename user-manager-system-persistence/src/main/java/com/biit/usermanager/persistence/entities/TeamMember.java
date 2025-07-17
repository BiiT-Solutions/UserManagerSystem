package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.CreatedElement;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "team_members", indexes = {
        @Index(name = "ind_team_member", columnList = "team_id"),
})
public class TeamMember extends CreatedElement {

    @EmbeddedId
    private TeamMemberId id;

    @Column(name = "organization_name")
    private String organizationName;

    public TeamMember() {
        super();
    }

    public TeamMember(Long teamId, Long userId, String organizationName, String createdBy) {
        this();
        setId(new TeamMemberId(teamId, userId));
        setOrganizationName(organizationName);
        setCreatedBy(createdBy);
    }

    public TeamMemberId getId() {
        return id;
    }

    public void setId(TeamMemberId id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TeamMember that = (TeamMember) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
