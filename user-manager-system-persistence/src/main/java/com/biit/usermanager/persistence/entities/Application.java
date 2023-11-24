package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "applications")
public class Application extends Element<String> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1082379190202846511L;

    @Id
    @Column(name = "name", length = MAX_UNIQUE_COLUMN_LENGTH)
    private String name;

    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description = "";

    public Application() {
        super();
    }

    public Application(String name) {
        this();
        setId(name);
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        this.name = id;
    }


    public String getName() {
        return getId();
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
                + "name='" + getName() + '\''
                + "}";
    }
}
