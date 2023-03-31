package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "organizations")
public class Organization extends Element {

    @Column(name = "name", nullable = false, unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    //A subOrganization is a company in USMO.
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parent")
    private Set<Organization> subOrganizations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Organization parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Organization> getSubOrganizations() {
        return subOrganizations;
    }

    public void setSubOrganizations(Set<Organization> subOrganizations) {
        this.subOrganizations = subOrganizations;
    }

    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parentOrganization) {
        this.parent = parentOrganization;
    }
}
