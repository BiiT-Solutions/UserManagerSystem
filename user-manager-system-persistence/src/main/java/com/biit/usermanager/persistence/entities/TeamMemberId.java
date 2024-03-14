package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class TeamMemberId implements Serializable {

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
}
