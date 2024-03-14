package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A team is an organization element to group workers and people. For role groups use the class 'UserGroup'.
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teams", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "organization_id"})},
        indexes = {
                @Index(name = "ind_parent", columnList = "parent_id"),
                @Index(name = "ind_organization", columnList = "organization_id"),
        })
public class Team extends Element<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Team parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getParent() {
        return parent;
    }

    public void setParent(Team parentTeam) {
        this.parent = parentTeam;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "Team{"
                + "name='" + name + '\''
                + ", parent=" + (parent != null ? parent.getId() : null)
                + "}";
    }
}
