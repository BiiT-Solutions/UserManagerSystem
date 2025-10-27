package com.biit.usermanager.core.providers;

/*-
 * #%L
 * User Manager System (core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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

    @Value("${user.password.recovery.token.expiration.time:48}")
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
            passwordResetToken.setExpirationDate(LocalDateTime.now().plusHours(tokenExpirationTimeInSeconds));
        }
        return super.save(passwordResetToken);
    }
}
