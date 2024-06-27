package com.biit.usermanager.core.providers;

import com.biit.logger.mail.SendEmail;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.PasswordResetToken;
import com.biit.usermanager.persistence.entities.User;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

@Service
public class EmailService {

    private static final String PASSWORD_RECOVERY_EMAIL_TEMPLATE = "reset-password.html";
    private static final String USER_CREATION_EMAIL_TEMPLATE = "user-created.html";
    private static final String EMAIL_LINK_TAG = "EMAIL:LINK";

    @Value("${mail.server.smtp.server:#{null}}")
    private String smtpServer;

    @Value("${mail.server.smtp.port:587}")
    private String smtpPort;

    @Value("${mail.server.smtp.username:#{null}}")
    private String emailUser;

    @Value("${mail.server.smtp.password:#{null}}")
    private String emailPassword;

    @Value("${mail.sender:#{null}}")
    private String emailSender;

    @Value("${mail.copy.address:#{null}}")
    private String mailCopy;

    @Value("${mail.password.recovery.subject:}")
    private String mailSubject;

    @Value("${mail.forgot.password.link:}")
    private String forgetPasswordEmailLink;

    @Value("${mail.user.creation.link:}")
    private String mailUserCreationLink;

    private final PasswordResetTokenProvider passwordResetTokenProvider;

    public EmailService(PasswordResetTokenProvider passwordResetTokenProvider) {
        this.passwordResetTokenProvider = passwordResetTokenProvider;
    }


    public void sendPasswordRecoveryEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (smtpServer != null && emailUser != null && forgetPasswordEmailLink != null) {
            sendTemplate(user, PASSWORD_RECOVERY_EMAIL_TEMPLATE, forgetPasswordEmailLink);
            UserManagerLogger.warning(this.getClass(), "Recovery password mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    public void sendUserCreationEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (smtpServer != null && emailUser != null && mailUserCreationLink != null) {
            sendTemplate(user, USER_CREATION_EMAIL_TEMPLATE, mailUserCreationLink);
            UserManagerLogger.warning(this.getClass(), "User creation mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    private void sendTemplate(User user, String passwordRecoveryEmailTemplate, String forgetPasswordEmailLink)
            throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        final String token = generateToken(user).getToken();
        final String emailTemplate = populateEmailFields(FileReader.getResource(passwordRecoveryEmailTemplate, StandardCharsets.UTF_8),
                forgetPasswordEmailLink, token);
        SendEmail.sendEmail(smtpServer, smtpPort, emailUser, emailPassword, emailSender, Collections.singletonList(user.getEmail()), null,
                mailCopy != null ? Collections.singletonList(mailCopy) : null, mailSubject,
                emailTemplate, null);
    }


    private PasswordResetToken generateToken(User user) {
        passwordResetTokenProvider.deleteByUser(user);
        final String token = UUID.randomUUID().toString();
        final PasswordResetToken userToken = new PasswordResetToken(token, user);
        return passwordResetTokenProvider.save(userToken);
    }


    private String populateEmailFields(String html, String link, String token) {
        return html.replace(EMAIL_LINK_TAG, link + ";token=" + token);
    }
}
