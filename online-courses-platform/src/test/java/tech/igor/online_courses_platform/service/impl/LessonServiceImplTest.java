package tech.igor.online_courses_platform.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import tech.igor.online_courses_platform.aws.S3Service;
import tech.igor.online_courses_platform.client.GumletService;
import tech.igor.online_courses_platform.client.dto.CreateAssetResponse;
import tech.igor.online_courses_platform.entities.Course;
import tech.igor.online_courses_platform.entities.Lesson;
import tech.igor.online_courses_platform.exceptions.CourseNotFoundException;
import tech.igor.online_courses_platform.exceptions.FileUploadException;
import tech.igor.online_courses_platform.repository.CourseRepository;
import tech.igor.online_courses_platform.repository.EnrollmentRepository;
import tech.igor.online_courses_platform.repository.LessonRepository;

import java.io.IOException;
import java.nio.channels.OverlappingFileLockException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private GumletService gumletService;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @Nested
    class PublishLesson {

        @Test
        void shouldThrowExceptionWhenCourseNotExists() {

            UUID courseId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(courseRepository).findById(courseId);

            var videoFile = mock(MultipartFile.class);

            var exception = assertThrows(
                    CourseNotFoundException.class, () -> {
                        lessonService.publishLesson(courseId, "Some title",
                                videoFile, 10);
                    }
            );

            assertEquals("Course not found with the provided ID", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenUploadToGumletFailed() throws IOException {

            var courseId = UUID.randomUUID();
            var course = new Course();
            course.setCourseId(courseId);
            course.setCollectionId("aaaaabbbb");

            var videoFile = mock(MultipartFile.class);
            when(videoFile.getOriginalFilename())
                    .thenReturn("video.mp4");

            var s3Url = "https://urlmock.com";
            var key = "courses/" + courseId + "/video.mp4";


            doReturn(Optional.of(course))
                    .when(courseRepository).findById(courseId);

            doReturn("https://s3.amazonaws.com/bucket/video.mp4")
                    .when(s3Service).uploadFile(videoFile, courseId);

            doReturn(s3Url)
                    .when(s3Service).generatePresignedUrl(key);

            doReturn(null)
                    .when(gumletService)
                    .uploadVideo(s3Url, course.getCollectionId());

            var exception = assertThrows(
                    FileUploadException.class, () -> {
                        lessonService.publishLesson(courseId, "Some title", videoFile, 1);
                    }
            );

            assertEquals("Upload to gumlet failed", exception.getMessage());
        }

        @Test
        void shouldPublishLessonSuccessfully() throws IOException {

            var courseId = UUID.randomUUID();
            var course = new Course();
            course.setCourseId(courseId);
            course.setCollectionId("aaaaabbbb");

            var videoFile = mock(MultipartFile.class);
            when(videoFile.getOriginalFilename())
                    .thenReturn("video.mp4");

            var s3Url = "https://urlmock.com";
            var key = "courses/" + courseId + "/video.mp4";

            var responseUrl = "https://videosalva.mp4.com";
            var assetResponse = new CreateAssetResponse(
                    "sasdjasdjads",
                    new CreateAssetResponse.Output(responseUrl)
            );


            doReturn(Optional.of(course))
                    .when(courseRepository).findById(courseId);

            doReturn("https://s3.amazonaws.com/bucket/video.mp4")
                    .when(s3Service).uploadFile(videoFile, courseId);

            doReturn(s3Url)
                    .when(s3Service).generatePresignedUrl(key);

            doReturn(assetResponse)
                    .when(gumletService)
                    .uploadVideo(s3Url, course.getCollectionId());

            var title = "Some title";
            var orderNumber = 1;

            var lesson = new Lesson();
            lesson.setTitle(title);
            lesson.setVideoUrl(responseUrl);
            lesson.setAssetId(assetResponse.assetId());
            lesson.setOrderNumber(orderNumber);
            lesson.setCourse(course);

            ArgumentCaptor<Lesson> lessonCaptor = ArgumentCaptor.forClass(Lesson.class);
            doAnswer(invocation -> invocation.getArgument(0))
                    .when(lessonRepository).save(any(Lesson.class));

            var response = lessonService.publishLesson(courseId, title, videoFile, orderNumber);

            verify(lessonRepository, times(1)).save(lessonCaptor.capture());
            var lessonValue = lessonCaptor.getValue();

            assertEquals(response, lessonValue);
            assertEquals(title, lessonValue.getTitle());
            assertEquals(responseUrl, lessonValue.getVideoUrl());
            assertEquals(assetResponse.assetId(), lessonValue.getAssetId());
            assertEquals(orderNumber, lessonValue.getOrderNumber());
            assertEquals(course, lessonValue.getCourse());
            verifyNoMoreInteractions(lessonRepository);
        }
    }

}