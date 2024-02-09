package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "user_groups_users", indexes = {
        @Index(name = "ind_usergroup_users", columnList = "user_group_id"),
})
public class UserGroupUsers extends StorableObject {

    @EmbeddedId
    private UserGroupUsersId id;

    public UserGroupUsers() {
        super();
    }

    public UserGroupUsers(Long userGroupId, Long userId) {
        this();
        setId(new UserGroupUsersId(userGroupId, userId));
    }

    public UserGroupUsersId getId() {
        return id;
    }

    public void setId(UserGroupUsersId id) {
        this.id = id;
    }
}
