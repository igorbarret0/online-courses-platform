package tech.igor.online_courses_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OnlineCoursesPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineCoursesPlatformApplication.class, args);
	}

}
