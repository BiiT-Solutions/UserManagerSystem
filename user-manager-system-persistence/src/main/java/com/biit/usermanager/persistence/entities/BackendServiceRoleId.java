package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Convert(converter = StringCryptoConverter.class)
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
