package com.biit.usermanager.persistence.repositories;

import com.biit.server.persistence.repositories.ElementRepository;
import com.biit.usermanager.persistence.entities.TemporalToken;
import com.biit.usermanager.persistence.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface TemporalTokenRepository extends ElementRepository<TemporalToken, Long> {

    Optional<TemporalToken> findByUser(User user);

    Optional<TemporalToken> findByContent(String content);

    void deleteByExpirationTimeBefore(LocalDateTime expirationTimeBefore);
}
