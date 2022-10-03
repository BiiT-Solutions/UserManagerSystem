package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.util.Optional;
@SpringBootTest
@Test(groups = {"organizationRepository"})

public class OrganizationRepositoryTest extends AbstractTestNGSpringContextTests {

    private static String ORGANIZATION_NAME = "TestName";

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    public void saveOrganization(){
        Organization organization = new Organization();
        organization.setName(ORGANIZATION_NAME);
        Assert.assertNull(organization.getId());
        organization = organizationRepository.save(organization);
        Assert.assertNotNull(organization.getId());
    }
    @Test(dependsOnMethods = "saveOrganization")
    public void getOrganizationByName(){
        Optional<Organization> organization = organizationRepository.findByName(ORGANIZATION_NAME);
        Assert.assertTrue(organization.isPresent());
        Assert.assertEquals(organization.get().getName(), ORGANIZATION_NAME);

    }

}
