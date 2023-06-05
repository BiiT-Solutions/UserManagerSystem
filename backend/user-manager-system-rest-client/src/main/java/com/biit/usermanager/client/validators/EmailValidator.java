package com.biit.usermanager.client.validators;

import com.biit.usermanager.client.exceptions.InvalidValueException;

import java.util.regex.Pattern;

public final class EmailValidator {

    private EmailValidator() {

    }

    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
            + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";

    private static final Pattern EMAIL_VALIDATOR = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValid(String email) {
        return EMAIL_VALIDATOR.matcher(email).matches();
    }

    public static String validate(String email) {
        if (!EMAIL_VALIDATOR.matcher(email).matches()) {
            throw new InvalidValueException(EmailValidator.class, "Email '" + email + "' is invalid.");
        }
        return email;
    }
}
