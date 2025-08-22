package tech.igor.online_courses_platform.controller.dtos;

import java.util.UUID;

public record UserLessonResponseDto(
        UUID lessonId,
        String title,
        String videoUrl,
        Integer orderNumber
) {
}
