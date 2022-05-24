package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "roles")
public class Role extends Element {

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
