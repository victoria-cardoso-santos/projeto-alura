package br.com.alura.ProjetoAlura.registration;

import org.springframework.stereotype.Repository;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
   boolean existsByUserAndCourse(User user, Course course);

   @Query(value = """
    SELECT 
        c.name AS course_name, 
        c.code AS course_code, 
        u.name AS instructor_name, 
        u.email AS instructor_email, 
        COUNT(r.id) AS total_registrations
    FROM 
        course c
    JOIN 
        registration r ON c.id = r.course_id
    JOIN 
        user u ON c.instructor_id = u.id
    GROUP BY 
        c.name, c.code, u.name, u.email
    ORDER BY 
        total_registrations DESC
    """, nativeQuery = true)
    List<Map<String, Object>> getMostAccessedCourses();

}
