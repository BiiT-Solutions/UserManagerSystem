package com.biit.usermanager.core.providers;

import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProvider extends CrudProvider<User, Long, UserRepository> {

    @Autowired
    public UserProvider(UserRepository repository) {
        super(repository);
    }
}
