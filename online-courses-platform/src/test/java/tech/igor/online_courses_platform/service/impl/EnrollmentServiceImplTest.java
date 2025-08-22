package tech.igor.online_courses_platform.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.igor.online_courses_platform.entities.Course;
import tech.igor.online_courses_platform.entities.Enrollment;
import tech.igor.online_courses_platform.entities.User;
import tech.igor.online_courses_platform.exceptions.CourseNotFoundException;
import tech.igor.online_courses_platform.repository.CourseRepository;
import tech.igor.online_courses_platform.repository.EnrollmentRepository;
import tech.igor.online_courses_platform.repository.UserRepository;
import tech.igor.online_courses_platform.security.CourseUserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Nested
    class EnrollCourse {

        @Test
        void shouldThrowExceptionWhenUserNotExists() {

            var user = new User();
            user.setEmail("user@email.com");
            var userDetails = new CourseUserDetails(user);
            var courseId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository).findByEmail(user.getEmail());

            var exception = assertThrows(
                    RuntimeException.class, () -> {
                        enrollmentService.enrollCourse(userDetails, courseId);
                    }
            );

            assertEquals("An error occurred", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenCourseNotExists() {

            var user = new User();
            user.setEmail("user@email.com");
            var userDetails = new CourseUserDetails(user);
            var courseId = UUID.randomUUID();

            doReturn(Optional.of(user))
                    .when(userRepository).findByEmail(user.getEmail());

            doReturn(Optional.empty())
                    .when(courseRepository).findById(courseId);


            var exception = assertThrows(
                    CourseNotFoundException.class, () -> {
                        enrollmentService.enrollCourse(userDetails, courseId);
                    }
            );

            assertEquals("Course not found with the provided ID: " + courseId,
                    exception.getMessage());
        }

        @Test
        void shouldEnrollCourseSuccessfully() {

            var user = new User();
            user.setEmail("user@email.com");
            var userDetails = new CourseUserDetails(user);
            var courseId = UUID.randomUUID();

            var course = new Course();
            course.setCourseId(courseId);
            course.setTitle("Some title");
            course.setDescription("Some description");

            doReturn(Optional.of(user))
                    .when(userRepository).findByEmail(user.getEmail());

            doReturn(Optional.of(course))
                    .when(courseRepository).findById(courseId);

            var enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);

            ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);

            enrollmentService.enrollCourse(userDetails, courseId);

            verify(enrollmentRepository, times(1)).save(enrollmentCaptor.capture());
            var enrollmentValue = enrollmentCaptor.getValue();

            assertEquals(enrollment.getUser().getEmail(),
                    enrollmentValue.getUser().getEmail());

            assertEquals(enrollment.getCourse().getCourseId(),
                    enrollmentValue.getCourse().getCourseId());

            assertEquals(enrollment.getCourse().getTitle(),
                    enrollmentValue.getCourse().getTitle());

            assertEquals(enrollment.getCourse().getDescription(),
                    enrollmentValue.getCourse().getDescription());

            verifyNoMoreInteractions(enrollmentRepository);
        }

    }

}