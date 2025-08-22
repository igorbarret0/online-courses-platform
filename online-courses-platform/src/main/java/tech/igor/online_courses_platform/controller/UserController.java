package tech.igor.online_courses_platform.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.igor.online_courses_platform.controller.dtos.CreateUserRequest;
import tech.igor.online_courses_platform.controller.dtos.CreateUserResponse;
import tech.igor.online_courses_platform.service.UserService;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {

        var response = userService.createUser(request);
        return ResponseEntity.ok(new CreateUserResponse(response.getUserId()));
    }

}
