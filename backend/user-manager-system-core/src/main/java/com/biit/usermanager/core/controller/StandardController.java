package com.biit.usermanager.core.controller;

import com.biit.usermanager.core.controller.models.Validates;
import com.biit.usermanager.core.providers.CrudProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public abstract class StandardController<ENTITY, DTO, REPOSITORY extends JpaRepository<ENTITY, Long>,
        PROVIDER extends CrudProvider<ENTITY, Long, REPOSITORY>> implements Validates<DTO> {
    protected final PROVIDER provider;

    protected StandardController(PROVIDER provider) {
        this.provider = provider;
    }

    public abstract Collection<DTO> get();

    public abstract DTO get(Long id);

    public void deleteById(Long id) {
        provider.deleteById(id);
    }

    public long count() {
        return provider.count();
    }
}
