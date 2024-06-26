package com.slatelog.slatelog.presentation;


import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.presentation.commands.Commands.UserRegistrationCommand;
import com.slatelog.slatelog.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.slatelog.slatelog.presentation.commands.Commands.UserVerificationCommand;

// Purpose of this class?
// --------------------------------------------------------------------------------------------
// This class is a Spring controller class for registering users.
// It handles HTTP requests and returns HTTP responses.

// Annotations used?
// --------------------------------------------------------------------------------------------
// @RestController marks this class that handles HTTP requests and returns JSON responses.
// @RequestMapping(..) maps HTTP requests with the given path to the controller methods.
// @RequiredArgsConstructor Lombok annotation that generates a constructor with all required fields.
// @RequestBody marks a method parameter as the body of the HTTP request.

// @RequestBody: Http Body (Json/XML) -> HTTP Message Converter -> Command Object
// @PathVariable HTTP Path -> Method Parameters
// @RequestParam HTTP Query Params -> Method Parameters
// @ModelAttribute Bind any Request objects -> Command

// HTTP Message Converter ?
// --------------------------------------------------------------------------------------------
// In Spring, Jackson is often used as the default message converter for handling JSON.
// It's known for its performance and flexibility in mapping Java objects to JSON and back.
// HTTP Response => Serialization (Object -> JSON)
// HTTP Request => Deserialization (JSON -> Object)

// Jackson Serialization
// --------------------------------------------------------------------------------------------
// During serialization, Jackson uses the getters of the Java object to convert it into JSON.
// It looks for methods that follow the JavaBean naming convention
// (e.g., getFieldName() for a field named fieldName).

// Jackson Deserialization
// --------------------------------------------------------------------------------------------
// For deserialization, Jackson uses the setters to convert the JSON to the Java object.
// It looks for setter methods that follow the JavaBean naming convention
// (e.g., setFieldName() for a field named fieldName).
// If no setter is available, Jackson can use other strategies,
// like direct field access or constructor-based deserialization,
// depending on the configuration.

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class UserRegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
    private final UserRegistrationService userRegistrationService;

    // HTTP Request:
    // POST /api/registration HTTP/1.1
    // Host: localhost:8080
    // Content-Type: application/json
    // Content-Length: xxx
    // {"email": "xxx", "password": "xxx", "firstName": "xxx", "lastName": "xxx"}

    // HTTP Response:
    // HTTP 1.1 CREATED
    // Location: /api/user/123
    // Body: { User }


    @PostMapping
    public ResponseEntity<User> register(@RequestBody UserRegistrationCommand command) {
        // TODO Just for testing, never ever log sensitive data !
        LOGGER.debug("User registration controller#register {}", command);
        var userRegistered = userRegistrationService.register(command);

        // Create Location Header
        // Location: /api/user/123
        URI uri = URI.create("/api/user/" + userRegistered.getId());
        return ResponseEntity.created(uri).body(userRegistered);
    }

    // HTTP Request:
    // GET /api/registration/verify?userId=123&tokenId=456" HTTP/1.1
    // Host: localhost:8080

    // HTTP Response:
    // HTTP 1.1 OK

    // PATH VERSION -----------------------------------------------------
    // Example: /api/registration/token/123/user/456
    // @GetMapping("/token/{tokenId}/user/{userId}")
    // public void verify(@PathVariable String tokenId, @PathVariable String userId) {
    //   var command = new UserVerificationCommand(userId, tokenId);
    //   userRegistrationService.verify(userId, tokenId);
    // }

    // REQUEST PARAM VERSION --------------------------------------------
    // Example: /api/registration/token?tokenId=456&userId=123
    // @GetMapping("/token")
    // public void verify(@RequestParam String tokenId, @RequestParam String userId) {
    //   var command = new UserVerificationCommand(userId, tokenId);
    //   userRegistrationService.verify(userId, tokenId);
    // }

    // MODEl ATTRIBUTE VERSION ------------------------------------------
    @GetMapping("/token")
    public void verify(@ModelAttribute UserVerificationCommand command) {
        LOGGER.debug("User registration controller#verify {}", command);
        userRegistrationService.verify(command);
    }
}
