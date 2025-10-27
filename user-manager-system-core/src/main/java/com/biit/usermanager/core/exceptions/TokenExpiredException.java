package com.biit.usermanager.core.exceptions;

/*-
 * #%L
 * User Manager System (core)
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
import com.biit.server.exceptions.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenExpiredException extends InvalidRequestException {

    @Serial
    private static final long serialVersionUID = 1940336335628874562L;

    public TokenExpiredException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public TokenExpiredException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public TokenExpiredException(Class<?> clazz) {
        this(clazz, "Token has expired!");
    }

    public TokenExpiredException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
