package com.biit.usermanager.rest.exceptions;

import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.server.logger.RestServerExceptionLogger;
import com.biit.usermanager.core.exceptions.ApplicationBackendRoleNotFoundException;
import com.biit.usermanager.core.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.exceptions.OrganizationAlreadyExistsException;
import com.biit.usermanager.core.exceptions.RoleWithoutBackendServiceRoleException;
import com.biit.usermanager.core.exceptions.TeamAlreadyExistsException;
import com.biit.usermanager.core.exceptions.TokenExpiredException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
import org.apache.kafka.common.errors.InvalidRequestException;
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
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> invalidRequestException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> userAlreadyExistsException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage(), ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> invalidPasswordException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleWithoutBackendServiceRoleException.class)
    public ResponseEntity<Object> roleWithoutBackendServiceRoleException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Role has no linked backend services. Backend services are necessary on each role."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrganizationAlreadyExistsException.class)
    public ResponseEntity<Object> organizationAlreadyExistsException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Organization already exists. ", ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<Object> teamAlreadyExistsException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Team already exists. ", ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> tokenExpiredException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApplicationBackendRoleNotFoundException.class)
    public ResponseEntity<Object> applicationBackendRoleNotFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotSentException.class)
    public ResponseEntity<Object> emailNotSentException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }


}
