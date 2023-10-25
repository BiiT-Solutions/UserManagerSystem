package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.repositories.BackendServiceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BackendServiceProvider extends ElementProvider<BackendService, Long, BackendServiceRepository> {

    public BackendServiceProvider(BackendServiceRepository repository) {
        super(repository);
    }

    public Optional<BackendService> findByName(String name) {
        return getRepository().findByName(name);
    }
}
