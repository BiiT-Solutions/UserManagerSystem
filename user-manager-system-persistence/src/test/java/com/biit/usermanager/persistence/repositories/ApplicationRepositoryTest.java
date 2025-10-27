package com.biit.usermanager.persistence.repositories;

/*-
 * #%L
 * User Manager System (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
        Application application = new Application(APPLICATION_NAME);
        application = applicationRepository.save(application);
        Assert.assertNotNull(application.getId());
    }

    @Test(dependsOnMethods = "saveApplication")
    public void getApplicationByName() {
        Optional<Application> application = applicationRepository.findById(APPLICATION_NAME);
        Assert.assertTrue(application.isPresent());
        Assert.assertEquals(application.get().getName(), APPLICATION_NAME);

    }

    @AfterClass(alwaysRun = true)
    public void wipeOut() {
        applicationRepository.deleteAll();
        Assert.assertEquals(applicationRepository.count(), 0);
    }
}
