package com.biit.usermanager.rest.exceptions;

import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.server.logger.RestServerExceptionLogger;
import com.biit.usermanager.core.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.exceptions.RoleWithoutBackendServiceRoleException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserManagerExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("NOT_FOUND", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> userAlreadyExistsException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Username already exists!", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> invalidPasswordException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("User not found for provided credentials"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleWithoutBackendServiceRoleException.class)
    public ResponseEntity<Object> roleWithoutBackendServiceRoleException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Role has no linked backend services. Backend services are necessary on each role."),
                HttpStatus.BAD_REQUEST);
    }
}
