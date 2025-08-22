package tech.igor.online_courses_platform.controller.dtos;

import java.util.List;
import java.util.UUID;

public record CourseResponseDto(
        UUID courseId,
        String title,
        String description,
        List<LessonResponseDto> lessons
) {
}
