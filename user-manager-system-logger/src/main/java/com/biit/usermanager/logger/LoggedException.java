package com.biit.usermanager.logger;

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
