package tech.igor.online_courses_platform.controller.dtos;

public record LoginResponse(

        String token,
        Long expiresIn
) {
}
