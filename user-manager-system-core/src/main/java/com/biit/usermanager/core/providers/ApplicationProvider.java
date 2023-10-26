package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationProvider extends ElementProvider<Application, String, ApplicationRepository> {

    @Autowired
    public ApplicationProvider(ApplicationRepository repository) {
        super(repository);
    }

    public Optional<Application> findByName(String name) {
        return getRepository().findById(name);
    }
}
