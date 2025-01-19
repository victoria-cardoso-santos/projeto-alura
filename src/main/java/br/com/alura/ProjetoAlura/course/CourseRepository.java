package br.com.alura.ProjetoAlura.course;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
    boolean existsByCode(String code);
}
