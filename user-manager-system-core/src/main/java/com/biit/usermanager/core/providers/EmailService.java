package com.biit.usermanager.core.providers;

import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.server.email.EmailSendPool;
import com.biit.server.email.ServerEmailService;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.usermanager.persistence.entities.PasswordResetToken;
import com.biit.usermanager.persistence.entities.User;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmailService extends ServerEmailService {

    //Templates are stored on BiiTRestServer project.
    private static final String PASSWORD_RECOVERY_EMAIL_TEMPLATE = "email-templates/key.html";
    private static final String USER_CREATION_EMAIL_TEMPLATE = "email-templates/key-holder.html";

    protected static final String EMAIL_LINK_TAG = "EMAIL:LINK";
    protected static final String EMAIL_BUTTON_TAG = "EMAIL:BUTTON";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("${mail.forgot.password.link:}")
    private String forgetPasswordEmailLink;

    @Value("${mail.user.creation.link:}")
    private String mailUserCreationLink;

    @Value("${mail.access.application.link:}")
    private String applicationLink;

    private final PasswordResetTokenProvider passwordResetTokenProvider;


    public EmailService(EmailSendPool emailSendPool, PasswordResetTokenProvider passwordResetTokenProvider, MessageSource messageSource) {
        super(emailSendPool, messageSource);
        this.passwordResetTokenProvider = passwordResetTokenProvider;
    }


    public void sendPasswordRecoveryEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (user != null && forgetPasswordEmailLink != null && !forgetPasswordEmailLink.isBlank()) {
            final String token = generateToken(user).getToken();
            final Locale locale = getUserLocale(user);
            final String emailTemplate = populatePasswordRecoveryEmailFields(FileReader.getResource(PASSWORD_RECOVERY_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    forgetPasswordEmailLink, token, locale);
            sendTemplate(user.getEmail(),
                    getMessage("forgotten.password.mail.subject", null, locale), emailTemplate,
                    getMessage("forgotten.password.mail.body", null, locale));
            UserManagerLogger.warning(this.getClass(), "Recovery password mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            UserManagerLogger.debug(this.getClass(), "Values are: forgetPasswordEmailLink '{}'.", forgetPasswordEmailLink);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    public void sendUserCreationEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (user != null && mailUserCreationLink != null && !mailUserCreationLink.isBlank()) {
            final String token = generateToken(user).getToken();
            final Locale locale = getUserLocale(user);
            final String bodyTag = user.getAccountExpirationTime() != null ? "new.user.mail.with.expiration.body" : "new.user.mail.body";
            final Object[] args = new Object[]{user.getName(), user.getLastname(), user.getUsername(),
                    user.getAccountExpirationTime() != null ? user.getAccountExpirationTime().format(formatter) : "",
                    applicationLink};
            final String emailTemplate = populateNewAccountCreatedEmailFields(FileReader.getResource(USER_CREATION_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    mailUserCreationLink, token, args, bodyTag, locale);
            sendTemplate(user.getEmail(), getMessage("new.user.mail.subject", args, locale), emailTemplate,
                    getMessage(bodyTag, args, locale));
            UserManagerLogger.warning(this.getClass(), "User creation mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            UserManagerLogger.debug(this.getClass(), "Values are: mailUserCreationLink '{}'.", mailUserCreationLink);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }

    private Locale getUserLocale(User user) {
        if (user.getLocale() != null && !user.getLocale().isBlank()) {
            return Locale.forLanguageTag(user.getLocale());
        }
        return Locale.ENGLISH;
    }


    private PasswordResetToken generateToken(User user) {
        passwordResetTokenProvider.deleteByUser(user);
        final String token = UUID.randomUUID().toString();
        final PasswordResetToken userToken = new PasswordResetToken(token, user);
        return passwordResetTokenProvider.save(userToken);
    }


    private String populatePasswordRecoveryEmailFields(String html, String link, String token, Locale locale) {
        return html.replace(EMAIL_LINK_TAG, link + ";token=" + token)
                .replace(EMAIL_TITLE_TAG, getMessage("forgotten.password.mail.title", null, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("forgotten.password.mail.subtitle", null, locale))
                .replace(EMAIL_BODY_TAG, getMessage("forgotten.password.mail.body", null, locale))
                .replace(EMAIL_BUTTON_TAG, getMessage("forgotten.password.mail.button", null, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("forgotten.password.mail.footer", null, locale));
    }


    private String populateNewAccountCreatedEmailFields(String html, String link, String token, Object[] args, String bodyTag, Locale locale) {
        return html.replace(EMAIL_LINK_TAG, link + ";token=" + token)
                .replace(EMAIL_TITLE_TAG, getMessage("new.user.mail.title", args, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("new.user.mail.subtitle", args, locale))
                .replace(EMAIL_BODY_TAG, getMessage(bodyTag, args, locale))
                .replace(EMAIL_SECOND_PARAGRAPH, getMessage("new.user.mail.second.paragraph", args, locale))
                .replace(EMAIL_BUTTON_TAG, getMessage("new.user.mail.button", args, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("new.user.mail.footer", args, locale));
    }
}
