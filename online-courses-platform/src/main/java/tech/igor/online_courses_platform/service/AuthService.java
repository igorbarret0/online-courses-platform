package tech.igor.online_courses_platform.service;

import org.springframework.security.core.userdetails.UserDetails;
import tech.igor.online_courses_platform.security.CourseUserDetails;

public interface AuthService {

    CourseUserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);

}
