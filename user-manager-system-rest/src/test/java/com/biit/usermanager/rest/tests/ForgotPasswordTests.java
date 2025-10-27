package com.biit.usermanager.rest.tests;

/*-
 * #%L
 * User Manager System (Rest)
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

import com.biit.server.security.model.AuthRequest;
import com.biit.usermanager.core.controller.UserController;
import com.biit.usermanager.core.converters.UserConverter;
import com.biit.usermanager.core.exceptions.UserNotFoundException;
import com.biit.usermanager.core.providers.PasswordResetTokenProvider;
import com.biit.usermanager.dto.UserDTO;
import com.biit.usermanager.persistence.entities.PasswordResetToken;
import com.biit.usermanager.rest.api.models.PasswordChangeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Test(groups = "forgotPasswordTests")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ForgotPasswordTests extends AbstractTestNGSpringContextTests {

    private static final String USER_NAME = "admin";
    private static final String USER_EMAIL = "admin@test.com";
    private static final String USER_UNIQUE_ID = "00000000AA";
    private static final String USER_FIRST_NAME = "Test";
    private static final String USER_LAST_NAME = "User";
    private static final String USER_PASSWORD = "asd123";
    private static final String USER_NEW_PASSWORD = "12345";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @Autowired
    private PasswordResetTokenProvider passwordResetTokenProvider;

    @Autowired
    private UserConverter userConverter;

    private MockMvc mockMvc;

    private UserDTO userDTO;

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }


    @BeforeClass
    public void createUser() {
        //Create the admin user
        userDTO = userController.createUser(USER_NAME, USER_UNIQUE_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_PASSWORD, null, null);
        userDTO.setEmail(USER_EMAIL);
        userController.update(userDTO, null);
    }

    @BeforeClass
    public void startServer() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void checkPassword() {
        userController.checkPassword(USER_NAME, USER_PASSWORD);
    }

    @Test(dependsOnMethods = "checkPassword")
    public void login() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(USER_NAME);
        request.setPassword(USER_PASSWORD);

        MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        String jwtToken = createResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Assert.assertNotNull(jwtToken);
    }

    @Test(dependsOnMethods = "login")
    public void forgotPassword() throws Exception {
        this.mockMvc
                .perform(get("/users/public/emails/" + USER_EMAIL + "/reset-password")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        //Cannot read the email, but can get token from database.
        PasswordResetToken passwordResetToken = passwordResetTokenProvider.findByUser(userConverter.reverse(userDTO))
                .orElseThrow(() -> new UserNotFoundException(this.getClass(), "Not working!"));

        this.mockMvc
                .perform(post("/users/public/change-password?token=" + passwordResetToken.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(new PasswordChangeRequest(USER_NEW_PASSWORD)))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        //Check new password.
        userController.checkPassword(USER_NAME, USER_NEW_PASSWORD);
    }
}
