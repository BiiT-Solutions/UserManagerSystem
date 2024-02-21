package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserGroupUserId implements Serializable {

    @Column(name = "user_group_id", nullable = false)
    private Long userGroupId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public UserGroupUserId() {
        super();
    }

    public UserGroupUserId(Long userGroupId, Long userId) {
        this();
        this.userGroupId = userGroupId;
        this.userId = userId;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
