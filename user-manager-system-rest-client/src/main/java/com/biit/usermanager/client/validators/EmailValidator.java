package com.biit.usermanager.client.validators;

/*-
 * #%L
 * User Manager System (Rest Client)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
