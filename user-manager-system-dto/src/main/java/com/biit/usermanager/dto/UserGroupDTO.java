package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserGroupDTO extends ElementDTO<Long> {

    private Long id;

    private String name = "";

    private String description = "";

    private Set<String> grantedAuthorities;

    private Collection<String> applicationRoles;

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

    public void setGrantedAuthorities(Set<String> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public void addGrantedAuthorities(String grantedAuthority) {
        if (this.grantedAuthorities == null) {
            this.grantedAuthorities = new HashSet<>();
        }
        this.grantedAuthorities.add(grantedAuthority);
    }

    public Collection<String> getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(Collection<String> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    public void addApplicationServiceRoles(String applicationServiceRoles) {
        if (this.applicationRoles == null) {
            this.applicationRoles = new HashSet<>();
        }
        this.applicationRoles.add(applicationServiceRoles);
    }

    public Set<String> getGrantedAuthorities() {
        return grantedAuthorities;
    }


    @Override
    public String toString() {
        return "UserGroupDTO{"
                + "name='" + name + '\''
                + '}';
    }
}
