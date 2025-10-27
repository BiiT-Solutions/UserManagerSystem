package com.biit.server.security.tests;

/*-
 * #%L
 * User Manager System (Test)
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


import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.server.client.SecurityClient;
import com.biit.usermanager.client.providers.AuthenticatedUserProvider;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.server.security.UserManagerServer;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.User;
import com.biit.usermanager.persistence.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = UserManagerServer.class)
@Test(groups = {"clientTests"})
public class ClientTests extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME = "admin";
    private static final String USER_PASSWORD = "asd123";
    private static final String JWT_SALT = "4567";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private SecurityClient securityClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserManagerClient userManagerClient;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @BeforeClass
    public void setAuthentication() {
        //Create the admin user for dummy authentication
        authenticatedUserProvider.createUser(USER_NAME, "", USER_PASSWORD);

        //Create same user for UserController used on the rest services tested later.
        final User admin = new User();
        admin.setUsername(USER_NAME);
        userRepository.save(admin);
    }

    @Test
    public void checkAuthentication() {
        //Check the admin user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER_NAME, JWT_SALT + USER_PASSWORD));
    }

    @Test
    public void checkClientWithSecurity() throws UnprocessableEntityException {
        Optional<UserDTO> user = userManagerClient.findByUsername(USER_NAME);
        Assert.assertTrue(user.isPresent());
    }
}
