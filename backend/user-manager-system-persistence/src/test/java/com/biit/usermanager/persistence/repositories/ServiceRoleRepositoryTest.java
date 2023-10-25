package com.biit.usermanager.persistence.repositories;


import com.biit.usermanager.persistence.entities.Service;
import com.biit.usermanager.persistence.entities.ServiceRole;
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
public class ServiceRoleRepositoryTest extends AbstractTestNGSpringContextTests {

    private static final String SERVICE_ROLE_NAME = "TestName";
    private static final String SERVICE_NAME = "ServiceName";

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceRoleRepository serviceRoleRepository;

    private Service service;

    @BeforeClass
    public void saveApplication() {
        service = new Service(SERVICE_NAME);
        Assert.assertNull(service.getId());
        service = serviceRepository.save(service);
        Assert.assertNotNull(service.getId());
    }

    @Test
    public void saveServiceRole() {
        ServiceRole serviceRole = new ServiceRole(service, SERVICE_ROLE_NAME);
        serviceRole = serviceRoleRepository.save(serviceRole);
        Assert.assertNotNull(serviceRole.getId());
    }

    @Test(dependsOnMethods = "saveServiceRole")
    public void getServiceRoleByName() {
        Optional<ServiceRole> serviceRole = serviceRoleRepository.findByIdServiceAndIdName(service, SERVICE_ROLE_NAME);
        Assert.assertTrue(serviceRole.isPresent());
        Assert.assertEquals(serviceRole.get().getName(), SERVICE_ROLE_NAME);
    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        serviceRoleRepository.deleteAll();
        serviceRepository.deleteAll();
        Assert.assertEquals(serviceRoleRepository.count(), 0);
        Assert.assertEquals(serviceRepository.count(), 0);
    }

}
