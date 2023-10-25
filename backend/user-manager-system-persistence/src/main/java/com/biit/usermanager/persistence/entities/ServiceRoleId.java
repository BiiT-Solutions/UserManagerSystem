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
public class ServiceRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 7130482204365537896L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "service")
    private Service service;

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name;

    public ServiceRoleId() {
        super();
    }

    public ServiceRoleId(Service service, String name) {
        super();
        setService(service);
        setName(name);
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
