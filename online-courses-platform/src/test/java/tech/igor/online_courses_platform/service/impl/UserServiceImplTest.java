package tech.igor.online_courses_platform.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.igor.online_courses_platform.controller.dtos.CreateUserRequest;
import tech.igor.online_courses_platform.controller.dtos.Role;
import tech.igor.online_courses_platform.entities.User;
import tech.igor.online_courses_platform.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    class CreateUser {

        @Test
        void shouldThrowExceptionWhenUserAlreadyExists() {

            var createUserRequest = new CreateUserRequest("user@email.com", "123", "user");

            var user = new User();
            user.setEmail("user@email.com");

            doReturn(Optional.of(user))
                    .when(userRepository).findByEmail(user.getEmail());

            var exception = assertThrows(RuntimeException.class, () -> {
                userService.createUser(createUserRequest);
            });

            assertEquals("A user with this email is already registered", exception.getMessage());
        }

        @Test
        void shouldCreateUserSuccessfully() {

            var userRequest = new CreateUserRequest(
                    "user@email.com",
                    "123",
                    "User"
            );

            doReturn(Optional.empty())
                    .when(userRepository).findByEmail(userRequest.email());

            var savedUser = new User();
            savedUser.setUserId(UUID.randomUUID());
            savedUser.setEmail(userRequest.email());
            savedUser.setName(userRequest.name());
            savedUser.setPassword(userRequest.password());
            savedUser.setRole(Role.ROLE_USER);

            doReturn(savedUser)
                    .when(userRepository).save(any(User.class));

            doReturn("encoded-password")
                    .when(passwordEncoder).encode(userRequest.password());

            var response = userService.createUser(userRequest);

            assertNotNull(response);
            assertEquals(savedUser.getEmail(), response.getEmail());
            assertEquals(savedUser.getName(), response.getName());
            assertEquals(savedUser.getRole(), response.getRole());

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());

            var capturedUser = captor.getValue();

            assertEquals(userRequest.email(), capturedUser.getEmail());
            assertEquals(userRequest.name(), capturedUser.getName());
            assertEquals(Role.ROLE_USER, capturedUser.getRole());
            assertNotNull(capturedUser.getPassword());
        }

    }

}