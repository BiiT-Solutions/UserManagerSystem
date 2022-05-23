package com.biit.usermanager.core.controller.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class ElementDTO {
    private Long id;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementDTO)) {
            return false;
        }
        final ElementDTO that = (ElementDTO) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedAt());
    }
}
