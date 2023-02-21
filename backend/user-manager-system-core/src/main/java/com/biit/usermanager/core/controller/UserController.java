package com.biit.usermanager.core.controller;

import com.biit.server.controller.BasicInsertableController;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.UserConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.core.providers.exceptions.InvalidParameterException;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public UserDTO getByUserName(String username) {
        return converter.convert(new UserConverterRequest(provider.findByUsername(username).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with username '" + username + "' found on the system."))));
    }

    public UserDTO updatePassword(String username, String oldPassword, String newPassword) {
        final UserDTO userDTO = converter.convert(new UserConverterRequest(provider.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(this.getClass(), "No User with username '" + username + "' found on the system."))));
        //Check old password.
        if (!BCrypt.checkpw(oldPassword, userDTO.getPassword())) {
            throw new InvalidParameterException(this.getClass(), "Provided password is incorrect!");
        }

        userDTO.setPassword(newPassword);
        return converter.convert(new UserConverterRequest(provider.save(converter.reverse(userDTO))));
    }
    public List<UserDTO> getByEnable(Boolean enable) {
        return provider.findAllByEnable(enable).parallelStream().map(this::createConverterRequest).map(converter::convert).collect(Collectors.toList());
    }

    public UserDTO getByPhone(String phone) {
        return converter.convert(new UserConverterRequest(provider.findByPhone(phone).orElseThrow(() -> new UserNotFoundException(this.getClass(),
                "No User with username '" + phone + "' found on the system."))));
    }

    public List<UserDTO> getAllByExpired(boolean accountExpired){
        final List<User> usersList = provider.findByAccountExpired(accountExpired);
        final List<UserDTO> usersdtList = new ArrayList<>();
        for (final User user : usersList){
            usersdtList.add(converter.convert(new UserConverterRequest(user)));
        }
        return usersdtList;
    }

    public void delete(User user){
        provider.delete(user);
    }

    public List<UserDTO> findAll(){
        return provider.findAll().parallelStream().map(this::createConverterRequest).map(converter::convert).collect(Collectors.toList());
    }
}
