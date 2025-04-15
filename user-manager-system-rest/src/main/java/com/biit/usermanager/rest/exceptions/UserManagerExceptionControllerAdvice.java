package com.biit.usermanager.rest.exceptions;

import com.biit.kafka.exceptions.InvalidEventException;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.server.exceptions.ErrorResponse;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.usermanager.core.exceptions.ApplicationBackendRoleNotFoundException;
import com.biit.usermanager.core.exceptions.ApplicationBackendServiceNotFoundException;
import com.biit.usermanager.core.exceptions.ApplicationNotFoundException;
import com.biit.usermanager.core.exceptions.BackendServiceRoleNotFoundException;
import com.biit.usermanager.core.exceptions.EmailAlreadyExistsException;
import com.biit.usermanager.core.exceptions.EmailNotFoundException;
import com.biit.usermanager.core.exceptions.ExternalReferenceAlreadyExistsException;
import com.biit.usermanager.core.exceptions.InvalidParameterException;
import com.biit.usermanager.core.exceptions.InvalidPasswordException;
import com.biit.usermanager.core.exceptions.OrganizationAlreadyExistsException;
import com.biit.usermanager.core.exceptions.OrganizationNotFoundException;
import com.biit.usermanager.core.exceptions.RoleWithoutBackendServiceRoleException;
import com.biit.usermanager.core.exceptions.TeamAlreadyExistsException;
import com.biit.usermanager.core.exceptions.TeamNotFoundException;
import com.biit.usermanager.core.exceptions.TemporalTokenDoesNotExistsException;
import com.biit.usermanager.core.exceptions.TokenExpiredException;
import com.biit.usermanager.core.exceptions.UserAlreadyExistsException;
import com.biit.usermanager.core.exceptions.UserDoesNotExistsException;
import com.biit.usermanager.core.exceptions.UserGroupNotFoundException;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserManagerExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> invalidRequestException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_request", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> userAlreadyExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "user_already_exists", ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> emailAlreadyExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "email_already_exists", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> invalidPasswordException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_password", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleWithoutBackendServiceRoleException.class)
    public ResponseEntity<Object> roleWithoutBackendServiceRoleException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Role has no linked backend services. Backend services are necessary on each role.",
                "role_without_backend_service_role"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrganizationAlreadyExistsException.class)
    public ResponseEntity<Object> organizationAlreadyExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Organization already exists.", "organization_already_exists", ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<Object> organizationNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Organization not found.", "organization_not_found", ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<Object> teamAlreadyExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Team already exists.", "team_already_exists", ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> tokenExpiredException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "token_expired"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApplicationBackendRoleNotFoundException.class)
    public ResponseEntity<Object> applicationBackendRoleNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "application_backend_role_not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApplicationBackendServiceNotFoundException.class)
    public ResponseEntity<Object> applicationBackendServiceNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "application_backend_service_not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<Object> applicationNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "application_not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BackendServiceRoleNotFoundException.class)
    public ResponseEntity<Object> backendServiceRoleNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "backend_service_role_not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotSentException.class)
    public ResponseEntity<Object> emailNotSentException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "email_not_sent"), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_credentials", ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    public ResponseEntity<Object> userDoesNotExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_credentials", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> emailNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "email_not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrganizationDoesNotExistException.class)
    public ResponseEntity<Object> organizationDoesNotExistException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "organization_does_not_exists", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TemporalTokenDoesNotExistsException.class)
    public ResponseEntity<Object> temporalTokenDoesNotExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "temporal_token_does_not_exists", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<Object> teamNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "team_not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserGroupNotFoundException.class)
    public ResponseEntity<Object> userGroupNotFoundException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "group_not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalReferenceAlreadyExistsException.class)
    public ResponseEntity<Object> externalReferenceAlreadyExistsException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "external_reference_already_exists", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Object> invalidParameterException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_parameter", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<Object> invalidEventException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "cannot_connect_to_kafka", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
