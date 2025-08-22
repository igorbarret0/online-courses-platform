package tech.igor.online_courses_platform.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.igor.online_courses_platform.client.GumletService;
import tech.igor.online_courses_platform.client.dto.CreateCollectionResponse;
import tech.igor.online_courses_platform.controller.dtos.CourseResponseDto;
import tech.igor.online_courses_platform.controller.dtos.CreateCourseDto;
import tech.igor.online_courses_platform.controller.dtos.LessonResponseDto;
import tech.igor.online_courses_platform.controller.dtos.Role;
import tech.igor.online_courses_platform.entities.Course;
import tech.igor.online_courses_platform.entities.Enrollment;
import tech.igor.online_courses_platform.entities.Lesson;
import tech.igor.online_courses_platform.entities.User;
import tech.igor.online_courses_platform.exceptions.UnauthorizedActionException;
import tech.igor.online_courses_platform.exceptions.UserNotFoundException;
import tech.igor.online_courses_platform.repository.CourseRepository;
import tech.igor.online_courses_platform.repository.EnrollmentRepository;
import tech.igor.online_courses_platform.repository.UserRepository;
import tech.igor.online_courses_platform.security.CourseUserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private GumletService gumletService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Nested
    class CreateCourse {

        @Test
        void shouldThrowExceptionWhenUserNotExists() {

            String email = "user@email.com";
            var dto = new CreateCourseDto(
                    "Course",
                    "Course for test"
            );

            Authentication authentication = mock(Authentication.class);
            when(authentication.getName())
                    .thenReturn(email);

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);

            doReturn(Optional.empty())
                    .when(userRepository).findByEmail(email);

            var exception = assertThrows(UserNotFoundException.class, () -> {
                courseService.createCourse(dto);
            });

            assertEquals("User could not be found", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenPublishCourseWithoutBeingAdmin() {


            var user = new User();
            user.setPassword("123");
            user.setName("User");
            String email = "user@email.com";
            user.setEmail(email);
            user.setRole(Role.ROLE_USER);

            var dto = new CreateCourseDto(
                    "Course",
                    "Course for test"
            );

            Authentication authentication = mock(Authentication.class);
            when(authentication.getName())
                    .thenReturn(email);

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);

            doReturn(Optional.of(user))
                    .when(userRepository).findByEmail(email);

            var exception = assertThrows(
                    UnauthorizedActionException.class, () -> {
                        courseService.createCourse(dto);
                    }
            );

            assertEquals("Only admins could publish a course", exception.getMessage());
        }

        @Test
        void shouldCreateCourseSuccessfully() {

            var user = new User();
            user.setPassword("123");
            user.setName("User");
            String email = "user@email.com";
            user.setEmail(email);
            user.setRole(Role.ROLE_ADMIN);

            var dto = new CreateCourseDto(
                    "Course",
                    "Course for test"
            );

            Authentication authentication = mock(Authentication.class);
            when(authentication.getName())
                    .thenReturn(email);

            SecurityContext securityContext = mock(SecurityContext.class);
            when(securityContext.getAuthentication())
                    .thenReturn(authentication);

            SecurityContextHolder.setContext(securityContext);

            doReturn(Optional.of(user))
                    .when(userRepository).findByEmail(email);

            var collectionResponse = new CreateCollectionResponse(
                    "09910281f2mkas"
            );

            doReturn(collectionResponse)
                    .when(gumletService).createCollection(dto.title());

            var savedCourse = new Course();
            savedCourse.setTitle(dto.title());
            savedCourse.setDescription(dto.description());
            savedCourse.setCollectionId(collectionResponse.id());
            savedCourse.setCreatedBy(user);

            doReturn(savedCourse)
                    .when(courseRepository).save(any(Course.class));

            var response = courseService.createCourse(dto);

            ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
            verify(courseRepository).save(captor.capture());
            var capturedCourse = captor.getValue();

            assertEquals(savedCourse, response);
            assertEquals(savedCourse.getTitle(), capturedCourse.getTitle());
            assertEquals(savedCourse.getDescription(), capturedCourse.getDescription());
            assertEquals(savedCourse.getCollectionId(), capturedCourse.getCollectionId());
            assertEquals(savedCourse.getCreatedBy(), capturedCourse.getCreatedBy());

        }

    }

    @Nested
    class GetLessonForUser {

        @Test
        void shouldReturnListOfUserCourseResponseDto() {

            var user = new User();
            user.setUserId(UUID.randomUUID());
            user.setEmail("use@email.com");
            user.setName("User");
            user.setPassword("123");

            var course = new Course();
            course.setCourseId(UUID.randomUUID());
            course.setTitle("Course");
            course.setCreatedBy(new User());
            course.setDescription("Course description");

            var lesson1 = new Lesson();
            lesson1.setLessonId(UUID.randomUUID());
            lesson1.setTitle("Lesson 1");
            lesson1.setVideoUrl("http://video1");
            lesson1.setOrderNumber(1);

            var lesson2 = new Lesson();
            lesson2.setLessonId(UUID.randomUUID());
            lesson2.setTitle("Lesson 2");
            lesson2.setVideoUrl("http://video2");
            lesson2.setOrderNumber(2);

            course.setLessons(List.of(lesson1, lesson2));

            var enrollment = new Enrollment();
            enrollment.setEnrollmentId(UUID.randomUUID());
            enrollment.setUser(user);
            enrollment.setCourse(course);

            var userDetails = new CourseUserDetails(user);

            List<Enrollment> enrollments = new ArrayList<>();
            enrollments.add(enrollment);

            doReturn(enrollments)
                    .when(enrollmentRepository).findByUser_UserId(user.getUserId());

            var response = courseService.getLessonsForUser(userDetails);

            assertNotNull(response);
            assertEquals(1, response.size());

            var courseDto = response.get(0);
            assertEquals(course.getCourseId(), courseDto.courseId());
            assertEquals(course.getTitle(), courseDto.title());
            assertEquals(course.getDescription(), courseDto.description());

            assertEquals(2, courseDto.lessons().size());

            var lessonDto1 = courseDto.lessons().get(0);
            assertEquals(lesson1.getLessonId(), lessonDto1.lessonId());
            assertEquals(lesson1.getTitle(), lessonDto1.title());
            assertEquals(lesson1.getVideoUrl(), lessonDto1.videoUrl());
            assertEquals(lesson1.getOrderNumber(), lessonDto1.orderNumber());

            var lessonDto2 = courseDto.lessons().get(1);
            assertEquals(lesson2.getLessonId(), lessonDto2.lessonId());
            assertEquals(lesson2.getTitle(), lessonDto2.title());
            assertEquals(lesson2.getVideoUrl(), lessonDto2.videoUrl());
            assertEquals(lesson2.getOrderNumber(), lessonDto2.orderNumber());
        }

    }

    @Nested
    class GetAllCourses {

        @Test
        void shouldReturnAllCourses() {

            var lesson1 = new Lesson();
            lesson1.setTitle("Aula 1");
            lesson1.setOrderNumber(1);

            var lesson2 = new Lesson();
            lesson2.setTitle("Aula 2");
            lesson2.setOrderNumber(2);

          var course = new Course();
          course.setCourseId(UUID.randomUUID());
          course.setTitle("Curso de Java");
          course.setDescription("Aprenda tudo sobre Java");
          course.setLessons(List.of(lesson1, lesson2));

          doReturn(List.of(course))
                  .when(courseRepository).findAll();

          var result = courseService.getAllCourses();

          assertEquals(1, result.size());

          var courseDto = result.get(0);

            assertEquals("Curso de Java", courseDto.title());
            assertEquals("Aprenda tudo sobre Java", courseDto.description());
            assertEquals(2, courseDto.lessons().size());
            assertEquals("Aula 1", courseDto.lessons().get(0).title());
            assertEquals(1, courseDto.lessons().get(0).orderNumber());

        }

    }

}