package com.biit.usermanager.core.providers;

import com.biit.logger.mail.SendEmail;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.PasswordResetToken;
import com.biit.usermanager.persistence.entities.User;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmailService {

    //Templates are stored on BiiTRestServer project.
    private static final String PASSWORD_RECOVERY_EMAIL_TEMPLATE = "email-templates/key.html";
    private static final String USER_CREATION_EMAIL_TEMPLATE = "email-templates/key-holder.html";

    private static final String EMAIL_LINK_TAG = "EMAIL:LINK";
    private static final String EMAIL_TITLE_TAG = "EMAIL:TITLE";
    private static final String EMAIL_SUBTITLE_TAG = "EMAIL:SUBTITLE";
    private static final String EMAIL_BODY_TAG = "EMAIL:BODY";
    private static final String EMAIL_BUTTON_TAG = "EMAIL:BUTTON";
    private static final String EMAIL_FOOTER_TAG = "EMAIL:FOOTER";

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

    @Value("${mail.forgot.password.link:}")
    private String forgetPasswordEmailLink;

    @Value("${mail.user.creation.link:}")
    private String mailUserCreationLink;

    private final PasswordResetTokenProvider passwordResetTokenProvider;

    private final MessageSource messageSource;

    public EmailService(PasswordResetTokenProvider passwordResetTokenProvider, MessageSource messageSource) {
        this.passwordResetTokenProvider = passwordResetTokenProvider;
        this.messageSource = messageSource;
    }


    public void sendPasswordRecoveryEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (smtpServer != null && emailUser != null && forgetPasswordEmailLink != null && !forgetPasswordEmailLink.isBlank()) {
            final String token = generateToken(user).getToken();
            final Locale locale = getUserLocale(user);
            final String emailTemplate = populatePasswordRecoveryEmailFields(FileReader.getResource(PASSWORD_RECOVERY_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    forgetPasswordEmailLink, token, locale);
            sendTemplate(user,
                    messageSource.getMessage("forgotten.password.mail.subject", null, locale), emailTemplate);
            UserManagerLogger.warning(this.getClass(), "Recovery password mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            UserManagerLogger.debug(this.getClass(), "Values are smtpServer '{}', emailUser '{}', forgetPasswordEmailLink '{}'.",
                    smtpServer, emailUser, forgetPasswordEmailLink);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    public void sendUserCreationEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (smtpServer != null && emailUser != null && mailUserCreationLink != null && !mailUserCreationLink.isBlank()) {
            final String token = generateToken(user).getToken();
            final Locale locale = getUserLocale(user);
            final String emailTemplate = populateNewAccountCreatedEmailFields(FileReader.getResource(USER_CREATION_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    mailUserCreationLink, token, locale);
            sendTemplate(user, messageSource.getMessage("new.user.mail.subject", null, locale), emailTemplate);
            UserManagerLogger.warning(this.getClass(), "User creation mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            UserManagerLogger.debug(this.getClass(), "Values are smtpServer '{}', emailUser '{}', mailUserCreationLink '{}'.",
                    smtpServer, emailUser, mailUserCreationLink);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }

    private Locale getUserLocale(User user) {
        if (user.getLocale() != null && !user.getLocale().isBlank()) {
            return Locale.forLanguageTag(user.getLocale());
        }
        return Locale.US;
    }


    private void sendTemplate(User user, String mailSubject, String emailTemplate)
            throws EmailNotSentException, InvalidEmailAddressException {
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


    private String populatePasswordRecoveryEmailFields(String html, String link, String token, Locale locale) {
        return html.replace(EMAIL_LINK_TAG, link + ";token=" + token)
                .replace(EMAIL_TITLE_TAG, messageSource.getMessage("forgotten.password.mail.title", null, locale))
                .replace(EMAIL_SUBTITLE_TAG, messageSource.getMessage("forgotten.password.mail.subtitle", null, locale))
                .replace(EMAIL_BODY_TAG, messageSource.getMessage("forgotten.password.mail.body", null, locale))
                .replace(EMAIL_BUTTON_TAG, messageSource.getMessage("forgotten.password.mail.button", null, locale))
                .replace(EMAIL_FOOTER_TAG, messageSource.getMessage("forgotten.password.mail.footer", null, locale));
    }


    private String populateNewAccountCreatedEmailFields(String html, String link, String token, Locale locale) {
        return html.replace(EMAIL_LINK_TAG, link + ";token=" + token)
                .replace(EMAIL_TITLE_TAG, messageSource.getMessage("new.user.mail.title", null, locale))
                .replace(EMAIL_SUBTITLE_TAG, messageSource.getMessage("new.user.mail.subtitle", null, locale))
                .replace(EMAIL_BODY_TAG, messageSource.getMessage("new.user.mail.body", null, locale))
                .replace(EMAIL_BUTTON_TAG, messageSource.getMessage("new.user.mail.button", null, locale))
                .replace(EMAIL_FOOTER_TAG, messageSource.getMessage("new.user.mail.footer", null, locale));
    }
}
