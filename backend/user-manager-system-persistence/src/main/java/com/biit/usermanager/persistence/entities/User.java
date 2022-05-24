package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.BooleanCryptoConverter;
import com.biit.database.encryption.LocalDateTimeCryptoConverter;
import com.biit.database.encryption.LocaleCryptoConverter;
import com.biit.database.encryption.StringCryptoConverter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users")
public class User extends Element {

    @Column(name = "id_card", unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String idCard;

    @Column(name = "username", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String username = "";

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    @Column(name = "lastname", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String lastname = "";

    @Column(name = "email", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String email = "";

    @Column(name = "phone", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String phone = "";

    @Column(name = "locale")
    @Convert(converter = LocaleCryptoConverter.class)
    private String locale = "";

    @Column(name = "password", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String password = "";

    @Column(name = "password_modified_date")
    @Convert(converter = LocalDateTimeCryptoConverter.class)
    private LocalDateTime passwordModifiedDate;

    @Column(name = "account_locked", nullable = false)
    @Convert(converter = BooleanCryptoConverter.class)
    private boolean accountLocked = false;

    @Column(name = "account_blocked", nullable = false)
    @Convert(converter = BooleanCryptoConverter.class)
    private boolean accountBlocked = false;

    @Column(name = "account_expired", nullable = false)
    @Convert(converter = BooleanCryptoConverter.class)
    private boolean accountExpired = false;

    @Column(name = "enabled", nullable = false)
    @Convert(converter = BooleanCryptoConverter.class)
    private boolean enabled = true;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUsername() {
        return username;
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPassword() {
        return password;
    }

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

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final User user = (User) o;
        return isAccountLocked() == user.isAccountLocked() && isAccountBlocked() == user.isAccountBlocked() &&
                Objects.equals(getIdCard(), user.getIdCard()) && getUsername().equals(user.getUsername()) &&
                getName().equals(user.getName()) && getLastname().equals(user.getLastname()) && getEmail().equals(user.getEmail()) &&
                Objects.equals(getLocale(), user.getLocale()) &&
                getPassword().equals(user.getPassword()) && Objects.equals(getPasswordModifiedDate(), user.getPasswordModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdCard(), getUsername(), getName(), getLastname(), getEmail(), getLocale(),
                getPassword(), getPasswordModifiedDate(), isAccountLocked(), isAccountBlocked());
    }
}
