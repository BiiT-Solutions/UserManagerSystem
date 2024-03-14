package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Application;
import com.biit.usermanager.persistence.entities.Team;
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

public class TeamRepositoryTest extends AbstractTestNGSpringContextTests {

    private static String GROUP_NAME = "TestName";
    private static String APPLICATION_NAME = "ApplicationName";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Application application;

    @BeforeClass
    public void saveApplication() {
        application = new Application(APPLICATION_NAME);
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());
    }

    @Test
    public void saveGroup() {
        Team team = new Team();
        team.setName(GROUP_NAME);
        team.setApplication(application);
        team = teamRepository.save(team);
        Assert.assertNotNull(team.getId());
    }

    @Test(dependsOnMethods = "saveGroup")
    public void getGroupByName() {
        Optional<Team> group = teamRepository.findByNameAndApplication(GROUP_NAME, application);
        Assert.assertTrue(group.isPresent());
        Assert.assertEquals(group.get().getName(), GROUP_NAME);
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        teamRepository.deleteAll();
        applicationRepository.deleteAll();
        Assert.assertEquals(applicationRepository.count(), 0);
        Assert.assertEquals(teamRepository.count(), 0);
    }

}
