package com.example.support.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class WelcomeController {

    @Operation(summary = "welcome basic auth", security = @SecurityRequirement(name = "basicAuth"))
    @GetMapping("")
    public String welcome(Authentication authentication) {
        log.info("Welcome to the Support Application! via basic auth {}", authentication.getName());
        return "Welcome to the Support Application!";
    }

    @Operation(summary = "welcome bearer token Auth", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/home")
    public String home(Authentication authentication) {
        log.info("Welcome back home! via bearer token Auth {}", authentication.getName());
        return "Welcome back home!";
    }

}
