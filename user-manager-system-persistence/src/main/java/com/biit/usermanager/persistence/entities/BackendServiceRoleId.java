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

@Embeddable
public class BackendServiceRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7130482204365537896L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "backend_service")
    private BackendService backendService;

    @Column(name = "name", nullable = false)
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
}
