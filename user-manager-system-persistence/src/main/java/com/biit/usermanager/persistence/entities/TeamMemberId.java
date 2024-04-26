package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TeamMemberId implements Serializable {

    private static final int HASH_CODE = 31;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public TeamMemberId() {
        super();
    }

    public TeamMemberId(Long teamId, Long userId) {
        this();
        this.teamId = teamId;
        this.userId = userId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long userGroupId) {
        this.teamId = userGroupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TeamMemberId that = (TeamMemberId) o;

        if (!teamId.equals(that.teamId)) {
            return false;
        }
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = teamId.hashCode();
        result = HASH_CODE * result + userId.hashCode();
        return result;
    }
}
