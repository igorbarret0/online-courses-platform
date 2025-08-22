package tech.igor.online_courses_platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.igor.online_courses_platform.controller.dtos.CourseResponseDto;
import tech.igor.online_courses_platform.controller.dtos.CreateCourseResponse;
import tech.igor.online_courses_platform.controller.dtos.UserCourseResponseDto;
import tech.igor.online_courses_platform.security.CourseUserDetails;
import tech.igor.online_courses_platform.service.CourseService;
import tech.igor.online_courses_platform.controller.dtos.CreateCourseDto;

import java.util.List;


@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CreateCourseResponse> createCourse(
            @RequestBody CreateCourseDto dto
    ) {

        var response = courseService.createCourse(dto);
        return ResponseEntity.ok(new CreateCourseResponse(response.getCourseId()));

    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<UserCourseResponseDto>> getUserCourses(
            Authentication authentication
    ) {

        var userDetails = (CourseUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(courseService.getLessonsForUser(userDetails));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {

        var allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }

}
