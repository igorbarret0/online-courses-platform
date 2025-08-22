package tech.igor.online_courses_platform.controller.dtos;

import java.util.UUID;

public record CreateCourseResponse(
        UUID courseId
) {
}
