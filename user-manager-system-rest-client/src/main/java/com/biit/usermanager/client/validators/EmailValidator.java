package com.biit.usermanager.client.validators;

import com.biit.usermanager.client.exceptions.InvalidValueException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public final class EmailValidator {

    private EmailValidator() {

    }

    public static boolean isValid(String email) {
        boolean result = true;
        try {
            final InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static String validate(String email) {
        try {
            final InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            throw new InvalidValueException(EmailValidator.class, "Email '" + email + "' is invalid.");
        }
        return email;
    }
}
