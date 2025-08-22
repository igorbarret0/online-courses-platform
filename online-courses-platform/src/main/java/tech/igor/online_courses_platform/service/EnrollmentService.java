package tech.igor.online_courses_platform.service;

import tech.igor.online_courses_platform.security.CourseUserDetails;

import java.util.UUID;

public interface EnrollmentService {

    void enrollCourse(CourseUserDetails userDetails, UUID courseId);

}
