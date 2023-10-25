package com.biit.server.security.tests;


import com.biit.rest.exceptions.UnprocessableEntityException;
import com.biit.server.client.SecurityClient;
import com.biit.server.security.AuthenticatedUserProvider;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.server.security.UserManagerServer;
import com.biit.usermanager.client.provider.UserManagerClient;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = UserManagerServer.class)
@Test(groups = {"clientTests"})
public class ClientTests extends AbstractTestNGSpringContextTests {
    private static final String USER_NAME = "admin";
    private static final String USER_PASSWORD = "asd123";
    private final static String JWT_SALT = "4567";

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
        Optional<IAuthenticatedUser> user = userManagerClient.findByUsername(USER_NAME);
        Assert.assertTrue(user.isPresent());
    }
}
