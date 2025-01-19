package br.com.alura.ProjetoAlura.course;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
    boolean existsByCode(String code);
    Optional<Course> findByCode(String code);
}
