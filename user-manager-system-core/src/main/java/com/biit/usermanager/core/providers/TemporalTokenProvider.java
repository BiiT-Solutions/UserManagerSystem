package com.biit.usermanager.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.usermanager.persistence.entities.TemporalToken;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.TemporalTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TemporalTokenProvider extends ElementProvider<TemporalToken, Long, TemporalTokenRepository> {

    private static final int RANDOM_MINIMUM_ASCII_CODE = 33;
    private static final int RANDOM_MAXIMUM_ASCII_CODE = 90;

    //Token expiration in minutes.
    private static final int TEMPORARY_TOKEN_DURATION = 2;

    private static final int TEMPORARY_LENGTH = 10;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    public TemporalTokenProvider(TemporalTokenRepository repository) {
        super(repository);
    }


    public static String generateRandomToken(int targetStringLength) {
        return RANDOM.ints(RANDOM_MINIMUM_ASCII_CODE, RANDOM_MAXIMUM_ASCII_CODE + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    public TemporalToken generateToken(User user) {
        final TemporalToken temporalToken = new TemporalToken(user, TEMPORARY_TOKEN_DURATION);
        getRepository().deleteByExpirationTimeBefore(LocalDateTime.now());
        final String content = generateRandomToken(TEMPORARY_LENGTH);
        final Optional<TemporalToken> existingToken = getRepository().findByContent(content);
        if (existingToken.isPresent()) {
            return generateToken(user);
        }
        temporalToken.setContent(content);
        return temporalToken;
    }


    public Optional<TemporalToken> findByUser(User user) {
        return getRepository().findByUser(user);
    }

}
