package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;

import java.io.Serial;
import java.time.LocalDateTime;

public class TemporalTokenDTO extends ElementDTO<Long> {

    @Serial
    private static final long serialVersionUID = 5910262810956281524L;

    private Long id;

    private Long userId;

    private String content;

    private LocalDateTime expirationTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
