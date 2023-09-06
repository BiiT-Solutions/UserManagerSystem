package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Set;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "groups")
public class Group extends Element {

    @Column(name = "name", nullable = false, unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    //A subGroup is a company in test.
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parent")
    private Set<Group> subGroups;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Group parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Group> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(Set<Group> subGroups) {
        this.subGroups = subGroups;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parentGroup) {
        this.parent = parentGroup;
    }

    @Override
    public String toString() {
        return "Group{"
                + "name='" + name + '\''
                + ", subGroups=" + subGroups
                + ", parent=" + parent
                + "}";
    }
}
