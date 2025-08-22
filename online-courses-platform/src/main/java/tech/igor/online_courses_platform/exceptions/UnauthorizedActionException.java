package tech.igor.online_courses_platform.exceptions;

public class UnauthorizedActionException extends RuntimeException{

    public UnauthorizedActionException(String message) {
        super(message);
    }

}
