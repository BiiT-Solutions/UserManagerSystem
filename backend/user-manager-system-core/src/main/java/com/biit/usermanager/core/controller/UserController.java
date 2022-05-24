package com.biit.usermanager.core.controller;

import com.biit.usermanager.core.controller.models.UserDTO;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController extends BasicInsertableController<User, UserDTO, UserRepository,
        UserProvider, UserConverterRequest, UserConverter> {

    @Autowired
    protected UserController(UserProvider provider, UserConverter converter) {
        super(provider, converter);
    }

    @Override
    protected UserConverterRequest createConverterRequest(User entity) {
        return new UserConverterRequest(entity);
    }
}
