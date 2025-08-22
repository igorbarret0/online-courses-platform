package tech.igor.online_courses_platform.service;

import org.springframework.web.multipart.MultipartFile;
import tech.igor.online_courses_platform.entities.Lesson;

import java.io.IOException;
import java.util.UUID;

public interface LessonService {

    Lesson publishLesson(UUID courseId, String title, MultipartFile videoFile,
                                Integer orderNumber) throws IOException;




}
