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
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService extends ServerEmailService {

    //Templates are stored on BiiTRestServer project.
    private static final String PASSWORD_RECOVERY_EMAIL_TEMPLATE = "email-templates/key.html";
    private static final String USER_CREATION_EMAIL_TEMPLATE = "email-templates/key-holder.html";
    private static final String USER_UPDATE_EMAIL_TEMPLATE = "email-templates/cauldron.html";

    protected static final String EMAIL_LINK_TAG = "EMAIL:LINK";
    protected static final String EMAIL_BUTTON_TAG = "EMAIL:BUTTON";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Value("#{new Boolean('${mail.server.enabled:true}')}")
    private boolean mailEnabled;

    @Value("${mail.forgot.password.link:}")
    private String forgetPasswordEmailLink;

    @Value("${mail.user.creation.link:}")
    private String mailUserCreationLink;

    @Value("${mail.access.application.link:}")
    private String applicationLink;

    @Value("#{new Boolean('${mail.updated.warning.email:false}')}")
    private boolean sendEmailOnUpdate = false;

    @Value("#{new Boolean('${mail.user.account.created:false}')}")
    private boolean sendEmailOnCreation = false;

    private final PasswordResetTokenProvider passwordResetTokenProvider;


    public EmailService(Optional<EmailSendPool> emailSendPool, PasswordResetTokenProvider passwordResetTokenProvider, MessageSource messageSource) {
        super(emailSendPool, messageSource);
        this.passwordResetTokenProvider = passwordResetTokenProvider;
    }


    public void sendPasswordRecoveryEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (user != null && forgetPasswordEmailLink != null && !forgetPasswordEmailLink.isBlank()) {
            final String token = generateToken(user).getToken();
            final Locale locale = getUserLocale(user);
            final Object[] args = emailArgs(user);
            final String emailTemplate = populatePasswordRecoveryEmailFields(FileReader.getResource(PASSWORD_RECOVERY_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    forgetPasswordEmailLink, token, args, locale);
            sendTemplate(user.getEmail(),
                    getMessage("forgotten.password.mail.subject", args, locale), emailTemplate,
                    getMessage("forgotten.password.mail.body", args, locale));
            UserManagerLogger.warning(this.getClass(), "Recovery password mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            UserManagerLogger.debug(this.getClass(), "Values are: forgetPasswordEmailLink '{}'.", forgetPasswordEmailLink);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    public void sendUserCreationEmail(User user) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (!mailEnabled) {
            return;
        }
        if (sendEmailOnCreation && user != null && user.getEmail() != null && mailUserCreationLink != null && !mailUserCreationLink.isBlank()) {
            final String token = generateToken(user).getToken();
            final Locale locale = getUserLocale(user);
            final String bodyTag = user.getAccountExpirationTime() != null ? "new.user.mail.with.expiration.body" : "new.user.mail.body";
            final Object[] args = emailArgs(user);
            final String emailTemplate = populateNewAccountCreatedEmailFields(FileReader.getResource(USER_CREATION_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    mailUserCreationLink, token, args, bodyTag, locale);
            sendTemplate(user.getEmail(), getMessage("new.user.mail.subject", args, locale), emailTemplate,
                    getMessage(bodyTag, args, locale) + "\n"
                            + (!applicationLink.isBlank() ? getMessage("new.user.mail.second.paragraph", args, locale) : ""));
            UserManagerLogger.warning(this.getClass(), "User creation mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            UserManagerLogger.debug(this.getClass(), "Values are: mailUserCreationLink '{}'.", mailUserCreationLink);
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }


    public void sendUserUpdateEmail(User user, String oldMail) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (!mailEnabled) {
            return;
        }
        if (sendEmailOnUpdate && user != null && oldMail != null) {
            UserManagerLogger.debug(this.getClass(), "Sending an email for change from '{}' to '{}'.", oldMail, user.getEmail());
            final Locale locale = getUserLocale(user);
            final String bodyTag = "update.user.mail.body";
            final Object[] args = emailArgs(user);
            final String emailTemplate = populateUpdateAccountCreatedEmailFields(FileReader.getResource(USER_UPDATE_EMAIL_TEMPLATE, StandardCharsets.UTF_8),
                    args, bodyTag, locale);
            sendTemplate(oldMail, getMessage("update.user.mail.subject", args, locale), emailTemplate,
                    getMessage(bodyTag, args, locale) + "\n"
                            + (!applicationLink.isBlank() ? getMessage("update.user.mail.second.paragraph", args, locale) : ""));
            UserManagerLogger.warning(this.getClass(), "User update mail send to '{}'.", user);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
            throw new EmailNotSentException("Email settings not set. Emails will be ignored.");
        }
    }

    private Object[] emailArgs(User user) {
        return new Object[]{user.getName(), user.getLastname(), user.getUsername(),
                user.getAccountExpirationTime() != null ? user.getAccountExpirationTime().format(FORMATTER) : "",
                applicationLink, user.getEmail()};
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


    private String populatePasswordRecoveryEmailFields(String html, String link, String token, Object[] args, Locale locale) {
        return html.replace(EMAIL_LINK_TAG, link + ";token=" + token)
                .replace(EMAIL_TITLE_TAG, getMessage("forgotten.password.mail.title", args, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("forgotten.password.mail.subtitle", args, locale))
                .replace(EMAIL_BODY_TAG, getMessage("forgotten.password.mail.body", args, locale))
                .replace(EMAIL_BUTTON_TAG, getMessage("forgotten.password.mail.button", args, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("forgotten.password.mail.footer", args, locale));
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

    private String populateUpdateAccountCreatedEmailFields(String html, Object[] args, String bodyTag, Locale locale) {
        return html.replace(EMAIL_TITLE_TAG, getMessage("update.user.mail.title", args, locale))
                .replace(EMAIL_SUBTITLE_TAG, getMessage("update.user.mail.subtitle", args, locale))
                .replace(EMAIL_BODY_TAG, getMessage(bodyTag, args, locale))
                .replace(EMAIL_SECOND_PARAGRAPH, getMessage("update.user.mail.second.paragraph", args, locale))
                .replace(EMAIL_FOOTER_TAG, getMessage("update.user.mail.footer", args, locale));
    }
}
