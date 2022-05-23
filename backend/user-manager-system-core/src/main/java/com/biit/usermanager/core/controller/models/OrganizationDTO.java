package com.biit.usermanager.core.controller.models;

import com.biit.usermanager.entity.IGroup;

public class OrganizationDTO extends ElementDTO implements IGroup<Long> {

    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public Long getUniqueId() {
        return getId();
    }
}
