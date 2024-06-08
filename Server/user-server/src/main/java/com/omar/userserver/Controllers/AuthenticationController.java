package com.omar.userserver.Controllers;


import com.omar.userserver.auth.AuthenticationRequest;
import com.omar.userserver.auth.AuthenticationResponse;
import com.omar.userserver.auth.AuthenticationService;
import com.omar.userserver.auth.RegistrationRequest;
import com.omar.userserver.dto.UserDto;
import com.omar.userserver.security.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    private final JwtService jwtService;



    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody   @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication connectedUser) {
        if (connectedUser == null || connectedUser.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<UserDto> userDtoOptional = service.getCurrentUser(connectedUser);
        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDtoOptional.get());
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<UserDto> getOwner(@PathVariable("ownerId") Integer ownerId) {
        UserDto userDto = service.getUserById(ownerId);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtService.isValid(token);
        return ResponseEntity.ok(isValid);
    }








}
