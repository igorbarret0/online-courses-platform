package tech.igor.online_courses_platform.exceptions;

public class CourseNotFoundException extends RuntimeException{

    public CourseNotFoundException(String message) {
        super(message);
    }

}
