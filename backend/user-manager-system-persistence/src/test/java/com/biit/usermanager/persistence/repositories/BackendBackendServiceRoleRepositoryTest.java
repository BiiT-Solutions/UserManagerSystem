package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.BackendService;
import com.biit.usermanager.persistence.entities.BackendServiceRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

@SpringBootTest
@Test(groups = {"serviceRoleRepository"})
public class BackendBackendServiceRoleRepositoryTest extends AbstractTestNGSpringContextTests {

    private static final String SERVICE_ROLE_NAME = "TestName";
    private static final String SERVICE_NAME = "ServiceName";

    @Autowired
    private BackendServiceRepository backendServiceRepository;

    @Autowired
    private ServiceRoleRepository serviceRoleRepository;

    private BackendService backendService;

    @BeforeClass
    public void saveApplication() {
        backendService = new BackendService(SERVICE_NAME);
        Assert.assertNull(backendService.getId());
        backendService = backendServiceRepository.save(backendService);
        Assert.assertNotNull(backendService.getId());
    }

    @Test
    public void saveServiceRole() {
        BackendServiceRole backendServiceRole = new BackendServiceRole(backendService, SERVICE_ROLE_NAME);
        backendServiceRole = serviceRoleRepository.save(backendServiceRole);
        Assert.assertNotNull(backendServiceRole.getId());
    }

    @Test(dependsOnMethods = "saveServiceRole")
    public void getServiceRoleByName() {
        Optional<BackendServiceRole> serviceRole = serviceRoleRepository.findByIdServiceAndIdName(backendService, SERVICE_ROLE_NAME);
        Assert.assertTrue(serviceRole.isPresent());
        Assert.assertEquals(serviceRole.get().getName(), SERVICE_ROLE_NAME);
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        serviceRoleRepository.deleteAll();
        backendServiceRepository.deleteAll();
        Assert.assertEquals(serviceRoleRepository.count(), 0);
        Assert.assertEquals(backendServiceRepository.count(), 0);
    }

}
