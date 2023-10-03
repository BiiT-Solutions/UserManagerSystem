package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Optional;

@SpringBootTest
@Test(groups = {"applicationRepository"})
public class ApplicationRepositoryTest extends AbstractTestNGSpringContextTests {

    private static String APPLICATION_NAME = "ApplicationName";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    public void saveApplication() {
        Application application = new Application();
        application.setName(APPLICATION_NAME);
        Assert.assertNull(application.getId());
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());
    }

    @Test(dependsOnMethods = "saveApplication")
    public void getApplicationByName() {
        Optional<Application> application = applicationRepository.findByName(APPLICATION_NAME);
        Assert.assertTrue(application.isPresent());
        Assert.assertEquals(application.get().getName(), APPLICATION_NAME);

    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        applicationRepository.deleteAll();
        Assert.assertEquals(applicationRepository.count(), 0);
    }
}
