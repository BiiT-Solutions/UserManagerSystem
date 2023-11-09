package com.biit.usermanager.dto;

import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class UserDTO extends BasicUserDTO implements IUser<Long>, IAuthenticatedUser {
    @Serial
    private static final long serialVersionUID = 6973886025199428759L;

    private String idCard;

    private String email = "";

    private String phone = "";

    private String address;

    private String postalCode;

    private String city;

    private String country;

    @JsonIgnore
    private Locale locale;

    @JsonIgnore
    private String password = "";

    private LocalDateTime passwordModifiedDate;

    private boolean accountLocked = false;

    private boolean accountBlocked = false;

    private boolean accountExpired = false;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    private Set<String> grantedAuthorities;

    private Collection<String> applicationRoles;

    @JsonIgnore
    @Override
    public String getMobilePhone() {
        return getPhone();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !accountExpired;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return !accountBlocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    @Override
    public String getEmailAddress() {
        return getEmail();
    }

    @Override
    public String getLanguageId() {
        return locale == null ? null : locale.toLanguageTag().replace("-", "_");
    }

    @JsonIgnore
    @Override
    public Locale getLocale() {
        return locale;
    }

    @JsonIgnore
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

    public void addGrantedAuthorities(String grantedAuthority) {
        if (this.grantedAuthorities == null) {
            this.grantedAuthorities = new HashSet<>();
        }
        this.grantedAuthorities.add(grantedAuthority);
    }

    public Collection<String> getApplicationRoles() {
        return applicationRoles;
    }

    public void setApplicationRoles(Collection<String> applicationRoles) {
        this.applicationRoles = applicationRoles;
    }

    public void addApplicationServiceRoles(String applicationServiceRoles) {
        if (this.applicationRoles == null) {
            this.applicationRoles = new HashSet<>();
        }
        this.applicationRoles.add(applicationServiceRoles);
    }

    public Set<String> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    @Override
    public String toString() {
        return "User{"
                + "username='" + getUsername() + '\''
                + "}";
    }
}
