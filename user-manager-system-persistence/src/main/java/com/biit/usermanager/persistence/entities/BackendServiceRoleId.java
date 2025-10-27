package com.biit.usermanager.persistence.entities;

/*-
 * #%L
 * User Manager System (Persistence)
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

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BackendServiceRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7130482204365537896L;

    protected static final int MAX_UNIQUE_COLUMN_LENGTH = 190;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "backend_service")
    private BackendService backendService;

    @Column(name = "name", nullable = false, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String name;

    public BackendServiceRoleId() {
        super();
    }

    public BackendServiceRoleId(BackendService backendService, String name) {
        super();
        setBackendService(backendService);
        setName(name);
    }

    public BackendService getBackendService() {
        return backendService;
    }

    public void setBackendService(BackendService backendService) {
        this.backendService = backendService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BackendServiceRoleId that = (BackendServiceRoleId) o;
        return Objects.equals(backendService, that.backendService) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backendService, name);
    }

    @Override
    public String toString() {
        return "BackendServiceRoleId{"
                + "backendService=" + backendService.getId()
                + ", name='" + name + '\''
                + '}';
    }
}
