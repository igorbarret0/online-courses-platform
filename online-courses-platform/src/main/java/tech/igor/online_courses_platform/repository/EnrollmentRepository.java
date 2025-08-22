package tech.igor.online_courses_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.igor.online_courses_platform.entities.Enrollment;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

   List<Enrollment> findByUser_UserId(UUID userId);

}
