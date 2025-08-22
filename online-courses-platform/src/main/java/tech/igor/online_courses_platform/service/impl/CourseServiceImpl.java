package tech.igor.online_courses_platform.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.igor.online_courses_platform.client.GumletService;
import tech.igor.online_courses_platform.controller.dtos.*;
import tech.igor.online_courses_platform.entities.Course;
import tech.igor.online_courses_platform.exceptions.UnauthorizedActionException;
import tech.igor.online_courses_platform.exceptions.UserNotFoundException;
import tech.igor.online_courses_platform.repository.CourseRepository;
import tech.igor.online_courses_platform.repository.EnrollmentRepository;
import tech.igor.online_courses_platform.repository.UserRepository;
import tech.igor.online_courses_platform.security.CourseUserDetails;
import tech.igor.online_courses_platform.service.CourseService;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GumletService gumletService;

    public CourseServiceImpl(UserRepository userRepository, CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository, GumletService gumletService) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.gumletService = gumletService;
    }


    @Override
    @Transactional
    public Course createCourse(CreateCourseDto dto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User could not be found"));

        if (!user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UnauthorizedActionException("Only admins could publish a course");
        }

        var collectionId = gumletService.createCollection(dto.title()).id();

        var course = new Course();
        course.setTitle(dto.title());
        course.setDescription(dto.description());
        course.setCollectionId(collectionId);
        course.setCreatedBy(user);

        return courseRepository.save(course);
    }

    @Override
    public List<UserCourseResponseDto> getLessonsForUser(CourseUserDetails userDetails) {

        var userId = userDetails.getId();

        var enrollments = enrollmentRepository.findByUser_UserId(userId);

        return enrollments.stream()
                .map(enrollment -> {
                    var course = enrollment.getCourse();
                    var lessons = course.getLessons().stream()
                            .map(lesson -> new UserLessonResponseDto(
                                    lesson.getLessonId(),
                                    lesson.getTitle(),
                                    lesson.getVideoUrl(),
                                    lesson.getOrderNumber()
                            ))
                            .toList();

                    return new UserCourseResponseDto(
                            course.getCourseId(),
                            course.getTitle(),
                            course.getDescription(),
                            lessons
                    );
                })
                .toList();
    }

    @Override
    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> new CourseResponseDto(
                        course.getCourseId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getLessons() != null
                                ? course.getLessons().stream()
                                .map(lesson -> new LessonResponseDto(
                                        lesson.getTitle(),
                                        lesson.getOrderNumber()
                                ))
                                .toList()
                                : List.of()
                    )).toList();
    }
}


