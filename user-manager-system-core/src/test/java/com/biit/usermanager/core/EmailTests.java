package com.biit.usermanager.core;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Test(groups = "emails")
public class EmailTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private MessageSource messageSource;

    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("language/language");
        return source;
    }

    @Test
    public void checkTranslations() {
        Assert.assertEquals(messageSource().getMessage("user.access.mail.title", null, Locale.ENGLISH), "Be cautious!");
    }

    @Test
    public void checkTranslationsBean() {
        Assert.assertEquals(messageSource.getMessage("user.access.mail.title", null, Locale.ENGLISH), "Be cautious!");
    }
}
