package tech.igor.online_courses_platform.service;

import tech.igor.online_courses_platform.controller.dtos.CreateUserRequest;
import tech.igor.online_courses_platform.entities.User;

public interface UserService {

    User createUser(CreateUserRequest request);

}
