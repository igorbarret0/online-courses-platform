package tech.igor.online_courses_platform.exceptions.dto;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;

    public ExceptionResponse() {}

    public ExceptionResponse(LocalDateTime timeStamp, int status, String error, String message) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
