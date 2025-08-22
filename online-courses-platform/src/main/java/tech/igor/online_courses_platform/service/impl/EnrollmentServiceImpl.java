package tech.igor.online_courses_platform.service.impl;

import org.springframework.stereotype.Service;
import tech.igor.online_courses_platform.entities.Enrollment;
import tech.igor.online_courses_platform.exceptions.CourseNotFoundException;
import tech.igor.online_courses_platform.repository.CourseRepository;
import tech.igor.online_courses_platform.repository.EnrollmentRepository;
import tech.igor.online_courses_platform.repository.UserRepository;
import tech.igor.online_courses_platform.security.CourseUserDetails;
import tech.igor.online_courses_platform.service.EnrollmentService;

import java.util.UUID;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void enrollCourse(CourseUserDetails userDetails, UUID courseId) {

        var user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("An error occurred"));

        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with the provided ID: " + courseId));

        var enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
    }
}
