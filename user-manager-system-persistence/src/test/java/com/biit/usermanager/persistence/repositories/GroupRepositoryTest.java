package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

@SpringBootTest
@Test(groups = {"groupRepository"})

public class GroupRepositoryTest extends AbstractTestNGSpringContextTests {

    private static String GROUP_NAME = "TestName";
    private static String APPLICATION_NAME = "ApplicationName";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private GroupRepository groupRepository;

    private Application application;

    @BeforeClass
    public void saveApplication() {
        application = new Application();
        application.setName(APPLICATION_NAME);
        Assert.assertNull(application.getId());
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());
    }

    @Test
    public void saveGroup() {
        Group group = new Group();
        group.setName(GROUP_NAME);
        Assert.assertNull(group.getId());
        group.setApplication(application);
        group = groupRepository.save(group);
        Assert.assertNotNull(group.getId());
    }

    @Test(dependsOnMethods = "saveGroup")
    public void getGroupByName() {
        Optional<Group> group = groupRepository.findByNameAndApplication(GROUP_NAME, application);
        Assert.assertTrue(group.isPresent());
        Assert.assertEquals(group.get().getName(), GROUP_NAME);
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        groupRepository.deleteAll();
        applicationRepository.deleteAll();
        Assert.assertEquals(applicationRepository.count(), 0);
        Assert.assertEquals(groupRepository.count(), 0);
    }

}
