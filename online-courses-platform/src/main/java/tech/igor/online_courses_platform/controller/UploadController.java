package tech.igor.online_courses_platform.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.igor.online_courses_platform.controller.dtos.UploadRequest;
import tech.igor.online_courses_platform.service.LessonService;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/uploads")
public class UploadController {

    private final LessonService lessonService;

    public UploadController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public ResponseEntity<String> uploadLesson(
            @RequestParam("title") String title,
            @RequestParam("courseId") UUID courseId,
            @RequestParam("orderNumber") Integer orderNumber,
            @RequestParam("videoFile") MultipartFile videoFile
    ) {

        try {
            var response = lessonService.publishLesson(
                    courseId,
                    title,
                    videoFile,
                    orderNumber
            );

            return ResponseEntity.ok(response.getVideoUrl());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
