package com.biit.usermanager.client.providers.models;

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
