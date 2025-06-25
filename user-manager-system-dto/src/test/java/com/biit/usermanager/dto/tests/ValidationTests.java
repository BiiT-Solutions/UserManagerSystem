package com.biit.usermanager.dto.tests;

import com.biit.usermanager.dto.UserDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

@Test(groups = "validationTests")
@SpringBootTest
public class ValidationTests {

    private Validator validator;

    @BeforeClass
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void invalidUserEmail() {
        UserDTO user = new UserDTO();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
        Assert.assertFalse(violations.isEmpty());
    }
}
