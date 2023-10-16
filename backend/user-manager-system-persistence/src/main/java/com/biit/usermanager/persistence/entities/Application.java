package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "applications")
public class Application extends Element {

    @Column(name = "name", nullable = false, unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description = "";


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

    @Override
    public String toString() {
        return "Application{"
                + "name='" + name + '\''
                + "}";
    }
}
