package com.biit.usermanager.core.providers;

import com.biit.logger.mail.SendEmail;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.logger.mail.exceptions.InvalidEmailAddressException;
import com.biit.usermanager.logger.UserManagerLogger;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
public class EmailService {

    private static final String PASSWORD_RECOVERY_EMAIL_TEMPLATE = "passwordRecoveryEmailTemplate.html";

    @Value("mail.server.smtp.server:null")
    private String smtpServer;

    @Value("mail.server.smtp.port:587")
    private String smtpPort;

    @Value("mail.server.smtp.username:null")
    private String emailUser;

    @Value("mail.server.smtp.password:null")
    private String emailPassword;

    @Value("mail.sender")
    private String emailSender;

    @Value("mail.copy.address")
    private String mailCopy;

    @Value("mail.password.recovery.subject")
    private String mailSubject;


    public void sendPasswordRecoveryEmail(String email) throws FileNotFoundException, EmailNotSentException, InvalidEmailAddressException {
        if (smtpServer == null || emailUser == null) {
            final String emailTemplate = FileReader.getResource(PASSWORD_RECOVERY_EMAIL_TEMPLATE, StandardCharsets.UTF_8);
            SendEmail.sendEmail(smtpServer, smtpPort, emailUser, emailPassword, emailSender, Collections.singletonList(email), null,
                    mailCopy != null ? Collections.singletonList(mailCopy) : null, mailSubject,
                    emailTemplate);
            UserManagerLogger.warning(this.getClass(), "Recovery password mail send to '{}'.", email);
        } else {
            UserManagerLogger.warning(this.getClass(), "Email settings not set. Emails will be ignored.");
        }
    }
}
