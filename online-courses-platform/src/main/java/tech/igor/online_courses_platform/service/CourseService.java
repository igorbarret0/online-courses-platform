package tech.igor.online_courses_platform.service;

import tech.igor.online_courses_platform.controller.dtos.CourseResponseDto;
import tech.igor.online_courses_platform.controller.dtos.UserCourseResponseDto;
import tech.igor.online_courses_platform.entities.Course;
import tech.igor.online_courses_platform.controller.dtos.CreateCourseDto;
import tech.igor.online_courses_platform.security.CourseUserDetails;

import java.util.List;

public interface CourseService {

    Course createCourse(CreateCourseDto dto);

    List<UserCourseResponseDto> getLessonsForUser(CourseUserDetails userDetails);

    List<CourseResponseDto> getAllCourses();


}
