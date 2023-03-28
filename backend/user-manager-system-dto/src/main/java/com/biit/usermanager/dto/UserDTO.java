package com.biit.usermanager.dto;

import com.biit.server.controllers.models.ElementDTO;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class UserDTO extends ElementDTO implements IUser<Long>, UserDetails, IAuthenticatedUser {
    private String idCard;

    private String username = "";

    private String name = "";

    private String lastname = "";

    private String email = "";

    private String phone = "";

    private Locale locale;

    private String password = "";

    private LocalDateTime passwordModifiedDate;

    private boolean accountLocked = false;

    private boolean accountBlocked = false;

    private boolean accountExpired = false;

    private boolean enabled = true;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    private Set<String> grantedAuthorities;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getFullName() {
        return (getFirstName() != null ? getFirstName() : "") +
                (getFirstName() != null && getLastName() != null ? " " : "") +
                (getLastName() != null ? getLastName() : "");
    }

    @Override
    public String getMobilePhone() {
        return getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmailAddress() {
        return getEmail();
    }

    @Override
    public String getFirstName() {
        return getName();
    }

    @Override
    public String getLanguageId() {
        return locale == null ? null : locale.toLanguageTag().replace("-", "_");
    }

    @Override
    public String getLastName() {
        return getLastname();
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        if (grantedAuthorities != null) {
            grantedAuthorities.forEach(grantedAuthority -> authorities.add(new SimpleGrantedAuthority(grantedAuthority)));
        }
        return authorities;
    }

    public void setGrantedAuthorities(Set<String> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public Set<String> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setFirstName(String firstName) {
        setName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        setLastname(lastName);
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getPasswordModifiedDate() {
        return passwordModifiedDate;
    }

    public void setPasswordModifiedDate(LocalDateTime passwordModifiedDate) {
        this.passwordModifiedDate = passwordModifiedDate;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isAccountBlocked() {
        return accountBlocked;
    }

    public void setAccountBlocked(boolean accountBlocked) {
        this.accountBlocked = accountBlocked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getUniqueName() {
        return getUsername();
    }

    @Override
    public Long getUniqueId() {
        return getId();
    }

    @Override
    public String getUID() {
        if (getId() != null) {
            return getId().toString();
        }
        return null;
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
}
