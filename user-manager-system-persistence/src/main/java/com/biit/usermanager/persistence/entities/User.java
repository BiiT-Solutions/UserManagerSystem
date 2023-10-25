package com.biit.usermanager.persistence.entities;

import com.biit.database.encryption.BCryptPasswordConverter;
import com.biit.database.encryption.BooleanCryptoConverter;
import com.biit.database.encryption.LocalDateCryptoConverter;
import com.biit.database.encryption.LocalDateTimeCryptoConverter;
import com.biit.database.encryption.StringCryptoConverter;
import com.biit.database.encryption.UUIDCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users", indexes = {
        @Index(name = "ind_username", columnList = "username"),
        @Index(name = "ind_email", columnList = "email")
})
public class User extends Element<Long> {
    private static final int UUID_COLUMN_LENGTH = 36;

    @Column(name = "id_card", unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String idCard;

    @Column(name = "user_uuid", unique = true, length = UUID_COLUMN_LENGTH, nullable = false)
    @Convert(converter = UUIDCryptoConverter.class)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "username", nullable = false, unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String username = "";

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";

    @Column(length = MAX_UNIQUE_COLUMN_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String initials;

    @Column(name = "lastname", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String lastname = "";

    @Column(name = "email", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String email = "";

    @Column(name = "birthdate")
    @Convert(converter = LocalDateCryptoConverter.class)
    private LocalDate birthdate;

    @Column(name = "phone", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String phone = "";

    @Column(name = "address", length = MAX_UNIQUE_COLUMN_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String address;

    @Column(name = "postal_code", length = MAX_UNIQUE_COLUMN_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String postalCode;

    @Column(name = "city", length = MAX_UNIQUE_COLUMN_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String city;

    @Column(name = "country", length = MAX_UNIQUE_COLUMN_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String country;

    @Column(name = "locale")
    @Convert(converter = StringCryptoConverter.class)
    private String locale = "";

    @Column(name = "password", nullable = false)
    @Convert(converter = BCryptPasswordConverter.class)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_by_application_service_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "application_service_role_application", referencedColumnName = "application_role_application"),
                    @JoinColumn(name = "application_service_role_role", referencedColumnName = "application_role_role"),
                    @JoinColumn(name = "application_service_role_service", referencedColumnName = "backend_service_role_service"),
                    @JoinColumn(name = "application_service_role_name", referencedColumnName = "backend_service_role_name"),
            })
    private List<ApplicationBackendServiceRole> applicationBackendServiceRoles;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
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

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<ApplicationBackendServiceRole> getApplicationServiceRoles() {
        return applicationBackendServiceRoles;
    }

    public void setApplicationServiceRoles(List<ApplicationBackendServiceRole> applicationBackendServiceRoles) {
        this.applicationBackendServiceRoles = applicationBackendServiceRoles;
    }

    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + "}";
    }
}
