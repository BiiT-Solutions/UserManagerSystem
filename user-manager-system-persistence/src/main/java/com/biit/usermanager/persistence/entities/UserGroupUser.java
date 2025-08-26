package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "user_groups_users", indexes = {
        @Index(name = "ind_usergroup_users", columnList = "user_group_id"),
})
public class UserGroupUser extends StorableObject {

    @Serial
    private static final long serialVersionUID = -5378190918989695625L;

    @EmbeddedId
    private UserGroupUserId id;

    public UserGroupUser() {
        super();
    }

    public UserGroupUser(Long userGroupId, Long userId) {
        this();
        setId(new UserGroupUserId(userGroupId, userId));
    }

    public UserGroupUserId getId() {
        return id;
    }

    public void setId(UserGroupUserId id) {
        this.id = id;
    }
}
