package com.biit.usermanager.core.controller;

import com.biit.server.controller.ElementController;
import com.biit.usermanager.core.converters.TemporalTokenConverter;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.converters.models.TemporalTokenConverterRequest;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.TemporalTokenProvider;
import com.biit.usermanager.core.providers.UserProvider;
import com.biit.usermanager.dto.TemporalTokenDTO;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.TemporalToken;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.TemporalTokenRepository;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class TemporalTokenController extends ElementController<TemporalToken, Long, TemporalTokenDTO, TemporalTokenRepository,
        TemporalTokenProvider, TemporalTokenConverterRequest, TemporalTokenConverter> {

    private final UserProvider userProvider;
    private final UserConverter userConverter;

    protected TemporalTokenController(TemporalTokenProvider provider, TemporalTokenConverter converter, UserProvider userProvider,
                                      UserConverter userConverter) {
        super(provider, converter);
        this.userProvider = userProvider;
        this.userConverter = userConverter;
    }

    @Override
    protected TemporalTokenConverterRequest createConverterRequest(TemporalToken temporalToken) {
        return new TemporalTokenConverterRequest(temporalToken);
    }

    public TemporalTokenDTO generateTemporalToken(UUID userUUID) {
        final User user = userProvider.findByUuid(userUUID).orElseThrow(
                () -> new UserNotFoundException(this.getClass(), "No user found with UUID '" + userUUID.toString() + "'."));
        return convert(generateTemporalToken(user));
    }

    public TemporalTokenDTO generateTemporalToken(UserDTO userDTO) {
        return convert(generateTemporalToken(userConverter.reverse(userDTO)));
    }

    private TemporalToken generateTemporalToken(User user) {
        if (user == null) {
            return null;
        }
        return getProvider().generateToken(user);
    }
}
