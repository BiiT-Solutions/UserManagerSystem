package com.biit.usermanager.client.providers.models;

import com.biit.logger.ExceptionType;
import com.biit.usermanager.client.exceptions.InvalidValueException;
import com.biit.usermanager.client.validators.EmailValidator;

public class Email {

    private String email;

    public Email() {
        super();
    }

    public Email(String email) {
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            this.email = null;
            return;
        }
        email = email.trim();
        if (!EmailValidator.isValid(email)) {
            throw new InvalidValueException(getClass(), "Email '" + email + "' is not a valid email.", ExceptionType.WARNING);
        }
        this.email = email.toLowerCase();
    }

    public Domain getDomain() {
        if (email == null) {
            return null;
        }
        final String[] mailParts = email.split("@");
        return mailParts.length > 1 ? new Domain(email.split("@")[1]) : null;
    }

    public String getUser() {
        return email.split("@")[0];
    }

    @Override
    public String toString() {
        return email;
    }
}
