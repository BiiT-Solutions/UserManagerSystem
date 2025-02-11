package com.biit.usermanager.persistence.entities;

import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.time.LocalDateTime;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "temporal_tokens", indexes = {
        @Index(name = "ind_user", columnList = "user"),
})
public class TemporalToken extends Element<Long> {

    //Token expiration in seconds.
    private static final int TEMPORARY_TOKEN_DURATION = 2;

    @Serial
    private static final long serialVersionUID = 3154687389600203979L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, unique = true)
    private String content;

    private LocalDateTime expirationTime;

    @OneToOne
    @JoinColumn(name = "user", nullable = false, unique = true)
    private User user;

    public TemporalToken() {
        super();
    }

    public TemporalToken(User user, int durationInMinutes) {
        this();
        setUser(user);
        setExpirationTime(LocalDateTime.now().plusMinutes(durationInMinutes));
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TemporalToken{"
                + content + '}';
    }
}
