package tech.igor.online_courses_platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.igor.online_courses_platform.security.CourseUserDetails;
import tech.igor.online_courses_platform.service.EnrollmentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping(path = "/{courseId}")
    public ResponseEntity<Void> enrollCourse(
            @PathVariable(name = "courseId") UUID courseId,
            Authentication authentication
    ) {

        var userDetails = (CourseUserDetails) authentication.getPrincipal();
        enrollmentService.enrollCourse(userDetails, courseId);
        return ResponseEntity.ok().build();

    }

}
