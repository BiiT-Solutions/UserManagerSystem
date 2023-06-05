package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;
@SpringBootTest
@Test(groups = {"groupRepository"})

public class GroupRepositoryTest extends AbstractTestNGSpringContextTests {

    private static String GROUP_NAME = "TestName";

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void saveGroup(){
        Group group = new Group();
        group.setName(GROUP_NAME);
        Assert.assertNull(group.getId());
        group = groupRepository.save(group);
        Assert.assertNotNull(group.getId());
    }
    @Test(dependsOnMethods = "saveGroup")
    public void getGroupByName(){
        Optional<Group> group = groupRepository.findByName(GROUP_NAME);
        Assert.assertTrue(group.isPresent());
        Assert.assertEquals(group.get().getName(), GROUP_NAME);

    }

}
