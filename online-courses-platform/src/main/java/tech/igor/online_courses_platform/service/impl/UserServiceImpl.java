package tech.igor.online_courses_platform.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.igor.online_courses_platform.controller.dtos.CreateUserRequest;
import tech.igor.online_courses_platform.controller.dtos.Role;
import tech.igor.online_courses_platform.entities.User;
import tech.igor.online_courses_platform.exceptions.UserAlreadyExistsException;
import tech.igor.online_courses_platform.repository.UserRepository;
import tech.igor.online_courses_platform.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("A user with this email is already registered");
        }

        var user = new User();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }

}
