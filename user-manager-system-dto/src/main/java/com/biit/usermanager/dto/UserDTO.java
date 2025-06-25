package com.biit.usermanager.dto;

import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends BasicUserDTO implements IUser<Long>, IAuthenticatedUser {
    @Serial
    private static final long serialVersionUID = 6973886025199428759L;

    private String idCard;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank
    private String email = "";

    private String phone = "";

    private String address;

    private String postalCode;

    private String city;

    private String country;

    private Locale locale;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password = "";

    private LocalDateTime passwordModifiedDate;

    private boolean accountLocked = false;

    private boolean accountBlocked = false;

    private LocalDateTime accountExpirationTime;

    private String externalReference;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    private Set<String> grantedAuthorities;

    private Collection<String> applicationRoles;

    public UserDTO() {
        super();
    }

    public UserDTO(String username, String name, String lastname, String email) {
        super(username, name, lastname);
        setEmail(email);
    }

    @JsonIgnore
    @Override
    public String getMobilePhone() {
        return getPhone();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return getAccountExpirationTime() == null || LocalDateTime.now().isBefore(getAccountExpirationTime());
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return !accountBlocked;
    }

    public String getEmail() {
        if (email == null) {
            return null;
        }
        return email.trim();
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
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean isAccountExpired() {
        return getAccountExpirationTime() != null && LocalDateTime.now().isAfter(getAccountExpirationTime());
    }

    @Override
    public LocalDateTime getAccountExpirationTime() {
        return accountExpirationTime;
    }

    public void setAccountExpirationTime(LocalDateTime accountExpirationTime) {
        this.accountExpirationTime = accountExpirationTime;
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

    @Override
    public String getPassword() {
        return password;
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

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    @Override
    public String toString() {
        return "User{"
                + "username='" + getUsername() + '\''
                + "}";
    }
}
