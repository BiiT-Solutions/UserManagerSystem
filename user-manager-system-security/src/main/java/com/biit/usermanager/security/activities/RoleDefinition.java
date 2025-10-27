package com.biit.usermanager.security.activities;

/*-
 * #%L
 * User Manager System (Security)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
        return "RoleDefinition{"
                + "name='" + name + '\''
                + ", activities=" + activities
                + '}';
    }
}
