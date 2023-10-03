package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "user_roles", indexes = {
        @Index(name = "ind_user", columnList = "user_role"),
        @Index(name = "ind_group", columnList = "user_group"),
        @Index(name = "ind_role", columnList = "role"),
})
public class UserRole extends Element {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group")
    private Group group;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "user_role", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "role", nullable = false)
    private Role role;


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRole{"
                + ", user=" + user
                + ", role=" + role
                + ", group=" + (group != null ? group.getId() : null)
                + "}";
    }
}
