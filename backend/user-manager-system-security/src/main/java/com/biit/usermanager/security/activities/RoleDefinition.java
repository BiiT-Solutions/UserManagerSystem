package com.biit.usermanager.security.activities;

import com.biit.usermanager.security.IActivity;

import java.util.Set;

public class RoleDefinition {
    private final String name;
    private final Set<IActivity> activities;
    private final String translationCode;

    public RoleDefinition(String name, Set<IActivity> activities, String translationCode) {
        super();
        this.name = name;
        this.activities = activities;
        this.translationCode = translationCode;
    }

    public String getName() {
        return name;
    }

    public Set<IActivity> getActivities() {
        return activities;
    }

    public String getTranslationCode() {
        return translationCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RoleDefinition other = (RoleDefinition) obj;
        if (name == null) {
            return other.name == null;
        } else {
            return name.equals(other.name);
        }
    }

    @Override
    public String toString() {
        return "RoleDefinition{" +
                "name='" + name + '\'' +
                ", activities=" + activities +
                '}';
    }
}
