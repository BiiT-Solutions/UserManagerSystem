package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public class BasicUserDTO extends ElementDTO<Long> implements IUser<Long>, IAuthenticatedUser {

    private Long id;

    @Serial
    private static final long serialVersionUID = -1718677380015570500L;

    private UUID uuid;

    @Size(min = ElementDTO.MIN_FIELD_LENGTH, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
    @NotBlank
    private String username = "";

    @Size(min = 1, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
    @NotNull
    private String name = "";

    @Size(min = 1, max = ElementDTO.MAX_NORMAL_FIELD_LENGTH)
    @NotNull
    private String lastname = "";

    private String externalReference;

    public BasicUserDTO() {
        uuid = UUID.randomUUID();
    }

    public BasicUserDTO(String username, String name, String lastname) {
        this();
        this.username = username;
        this.name = name;
        this.lastname = lastname;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    @JsonIgnore
    @Override
    public String getUID() {
        if (uuid == null) {
            return null;
        }
        return uuid.toString();
    }

    @JsonIgnore
    public void setUID(String uid) {
        if (uid == null) {
            this.uuid = null;
        } else {
            this.uuid = UUID.fromString(uid);
        }
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @JsonIgnore
    @Override
    public String getFullName() {
        return (getFirstName() != null ? getFirstName() : "")
                + (getFirstName() != null && getLastName() != null ? " " : "")
                + (getLastName() != null ? getLastName() : "");
    }

    @JsonIgnore
    @Override
    public String getMobilePhone() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getEmailAddress() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getFirstName() {
        return getName();
    }

    public String getFirstname() {
        return getName();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Override
    public String getLastName() {
        return lastname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public boolean isAccountExpired() {
        return false;
    }

    @Override
    public LocalDateTime getAccountExpirationTime() {
        return null;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @JsonIgnore
    @Override
    public void setFirstName(String firstname) {
        this.name = firstname;
    }

    public void setFirstname(String firstname) {
        this.name = firstname;
    }

    @JsonIgnore
    @Override
    public void setLastName(String surname) {
        this.lastname = surname;
    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public void setPassword(String password) {

    }

    @JsonIgnore
    @Override
    public String getUniqueName() {
        return getUsername();
    }

    @JsonIgnore
    @Override
    public Long getUniqueId() {
        return getId();
    }

    @Override
    public int compareTo(IUser<Long> user) {
        // Compare by surname.
        if (this.getLastName() == null) {
            if (user.getLastName() != null) {
                return -1;
            }
        } else {
            if (user.getLastName() == null) {
                return 1;
            }
        }
        final int lastNameComparator = getLastName().compareTo(user.getLastName());
        if (lastNameComparator != 0) {
            return lastNameComparator;
        }
        // Compare by name.
        if (this.getFirstName() == null) {
            if (user.getFirstName() != null) {
                return -1;
            }
        } else {
            if (user.getFirstName() == null) {
                return 1;
            }
        }
        return getFirstName().compareTo(user.getFirstName());
    }

    @Override
    public String toString() {
        return "UserDTO{"
                + "username='" + username + '\''
                + "}";
    }
}
