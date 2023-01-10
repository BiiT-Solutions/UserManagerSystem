package com.biit.usermanager.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationProvider extends CrudProvider<Application, Long, ApplicationRepository> {

    @Autowired
    public ApplicationProvider(ApplicationRepository repository) {
        super(repository);
    }
}
