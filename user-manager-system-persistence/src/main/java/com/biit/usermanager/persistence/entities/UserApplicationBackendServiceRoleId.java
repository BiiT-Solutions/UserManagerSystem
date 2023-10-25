package com.biit.usermanager.persistence.entities;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

@Embeddable
public class UserApplicationBackendServiceRoleId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "application_backend_service_role_application", nullable = false)
    private Long applicationId;

    @Column(name = "application_backend_service_role_role", nullable = false)
    private Long roleId;

    @Column(name = "application_backend_service_role_service", nullable = false)
    private Long backendServiceId;

    @Column(name = "application_backend_service_role_name", nullable = false)
    private String backendServiceRole;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getBackendServiceId() {
        return backendServiceId;
    }

    public void setBackendServiceId(Long backendServiceId) {
        this.backendServiceId = backendServiceId;
    }

    public String getBackendServiceRole() {
        return backendServiceRole;
    }

    public void setBackendServiceRole(String backendServiceRole) {
        this.backendServiceRole = backendServiceRole;
    }
}
