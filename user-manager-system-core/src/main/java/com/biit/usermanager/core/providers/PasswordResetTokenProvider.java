package com.biit.usermanager.core.providers;

import com.biit.server.providers.StorableObjectProvider;
import com.biit.usermanager.persistence.entities.PasswordResetToken;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetTokenProvider extends StorableObjectProvider<PasswordResetToken, Long, PasswordResetTokenRepository> {

    @Value("${user.password.recovery.token.expiration.time:600}")
    private Long tokenExpirationTimeInSeconds;


    @Autowired
    public PasswordResetTokenProvider(PasswordResetTokenRepository repository) {
        super(repository);
    }


    public Optional<PasswordResetToken> findByToken(String token) {
        return getRepository().findByToken(token);
    }

    public Optional<PasswordResetToken> findByUser(User user) {
        return getRepository().findByUser(user);
    }

    public Optional<PasswordResetToken> deleteByUser(User user) {
        return getRepository().deleteByUser(user);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        if (passwordResetToken.getExpirationDate() == null) {
            passwordResetToken.setExpirationDate(LocalDateTime.now().plusSeconds(tokenExpirationTimeInSeconds));
        }
        return super.save(passwordResetToken);
    }
}
