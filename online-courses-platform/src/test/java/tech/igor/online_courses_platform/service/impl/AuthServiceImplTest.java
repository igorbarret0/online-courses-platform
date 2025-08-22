package tech.igor.online_courses_platform.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import tech.igor.online_courses_platform.entities.User;
import tech.igor.online_courses_platform.security.CourseUserDetails;
import tech.igor.online_courses_platform.security.CourseUserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CourseUserDetailsService courseUserDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    class Authenticate {

        @Test
        void shouldAuthenticateAndReturnUserDetails() {

            String email = "user@email.com";
            String password = "123";

            var user = new User();
            user.setName("User");
            user.setEmail(email);
            user.setPassword(password);

            var expectedUser = new CourseUserDetails(user);

            doReturn(expectedUser)
                    .when(courseUserDetailsService).loadUserByUsername(email);

            var result = authService.authenticate(email, password);

            assertNotNull(result);
            assertEquals(expectedUser, result);

            verify(authenticationManager).authenticate(
                    argThat(auth -> {
                        assertEquals(email, auth.getPrincipal());
                        assertEquals(password, auth.getCredentials());
                        return true;
                    })
            );

            verify(courseUserDetailsService).loadUserByUsername(email);
        }

    }

}