package com.biit.usermanager.core.providers;


import com.biit.server.providers.CrudProvider;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProvider extends CrudProvider<User, Long, UserRepository> {

    @Autowired
    public UserProvider(UserRepository repository) {
        super(repository);
    }

    public Optional<User> findByUsername(String username) {
        return getRepository().findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return getRepository().findByEmail(email);
    }


    public Optional<User> getById(String uuid) {
        try {
            return getById(Long.parseLong(uuid));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public Optional<User> getById(Long id) {
        return getRepository().findById(id);
    }


    public List<User> findAllByEnable(boolean enabled) {

        return getRepository().findAllByEnabled(enabled);
    }

    public Optional<User> findByPhone(String phone) {
        return getRepository().findByPhone(phone);
    }

    public List<User> findByAccountExpired(boolean accountExpired) {
        return getRepository().findByAccountExpired(accountExpired);
    }

    public List<User> findAll() {
        return getRepository().findAll();
    }

    public Optional<User> deleteByUsername(String username) {
        return getRepository().deleteByUsername(username);
    }


}
