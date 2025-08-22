package tech.igor.online_courses_platform.controller.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @Email(message = "Invalid email") String email,
        @Size(min = 8, max = 12, message = "The password must contain 8-12 characters") String password,
        String name
) {
}
