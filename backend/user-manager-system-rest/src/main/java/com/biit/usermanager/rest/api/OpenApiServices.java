package com.biit.usermanager.rest.api;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class OpenApiServices {

    @Hidden
    @Operation(summary = "Redirects root address to API web site.")
    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public void root(HttpServletResponse response, HttpServletRequest httpRequest) throws IOException {
        response.sendRedirect("./swagger-ui/index.html");
    }
}
