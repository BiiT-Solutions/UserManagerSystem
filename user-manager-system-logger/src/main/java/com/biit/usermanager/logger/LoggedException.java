package com.biit.usermanager.logger;

/*-
 * #%L
 * User Manager System (Logger)
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
import org.springframework.http.HttpStatus;

public abstract class LoggedException extends RuntimeException {
    private static final long serialVersionUID = -2118048384077287599L;
    private HttpStatus status;

    public LoggedException(Class<?> clazz, String message, ExceptionType type, HttpStatus status) {
        super(message);
        this.status = status;
        final String className = clazz.getName();
        switch (type) {
            case INFO:
                UserManagerLogger.info(className, message);
                break;
            case WARNING:
                UserManagerLogger.warning(className, message);
                break;
            case SEVERE:
                UserManagerLogger.severe(className, message);
                break;
            default:
                UserManagerLogger.debug(className, message);
                break;
        }
    }

    public LoggedException(Class<?> clazz, Throwable e, HttpStatus status) {
        this(clazz, e);
        this.status = status;
    }

    public LoggedException(Class<?> clazz, Throwable e) {
        super(e);
        UserManagerLogger.errorMessage(clazz, e);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
