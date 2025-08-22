package tech.igor.online_courses_platform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.igor.online_courses_platform.aws.S3Service;
import tech.igor.online_courses_platform.client.GumletService;
import tech.igor.online_courses_platform.client.dto.CreateAssetResponse;
import tech.igor.online_courses_platform.entities.Lesson;
import tech.igor.online_courses_platform.exceptions.CourseNotFoundException;
import tech.igor.online_courses_platform.exceptions.FileUploadException;
import tech.igor.online_courses_platform.repository.CourseRepository;
import tech.igor.online_courses_platform.repository.EnrollmentRepository;
import tech.igor.online_courses_platform.repository.LessonRepository;
import tech.igor.online_courses_platform.service.LessonService;

import java.io.IOException;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    private final S3Service s3Service;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GumletService gumletService;

    public LessonServiceImpl(S3Service s3Service, LessonRepository lessonRepository,
                             CourseRepository courseRepository, GumletService gumletService,
                             EnrollmentRepository enrollmentRepository) {
        this.s3Service = s3Service;
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.gumletService = gumletService;
        this.enrollmentRepository = enrollmentRepository;
    }


    @Override
    public Lesson publishLesson(UUID courseId, String title, MultipartFile videoFile,
                                Integer orderNumber) throws IOException {

        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with the provided ID"));

        String key = "courses/" + courseId + "/" + videoFile.getOriginalFilename();
        s3Service.uploadFile(videoFile, courseId);

        String s3Url = s3Service.generatePresignedUrl(key);

        CreateAssetResponse response = gumletService.uploadVideo(s3Url, course.getCollectionId());

        if (response == null) throw new FileUploadException("Upload to gumlet failed");

        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setVideoUrl(response.output().playbackUrl());
        lesson.setAssetId(response.assetId());
        lesson.setOrderNumber(orderNumber);
        lesson.setCourse(course);

        return lessonRepository.save(lesson);
    }




}
