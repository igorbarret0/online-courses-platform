package tech.igor.online_courses_platform.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.igor.online_courses_platform.controller.dtos.LoginRequest;
import tech.igor.online_courses_platform.controller.dtos.LoginResponse;
import tech.igor.online_courses_platform.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {

        var userDetails = authService.authenticate(request.email(), request.password());
        String token = authService.generateToken(userDetails);

        var response = new LoginResponse(token, 86400L);

        return ResponseEntity.ok(response);
    }

}
