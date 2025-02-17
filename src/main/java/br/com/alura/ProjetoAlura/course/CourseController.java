package br.com.alura.ProjetoAlura.course;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;

import static br.com.alura.ProjetoAlura.user.Role.INSTRUCTOR;
import static br.com.alura.ProjetoAlura.course.Status.INACTIVE;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        if(courseRepository.existsByCode(newCourse.getCode())) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Código do curso já cadastrado no sistema."));
        }

        Optional<User> userOptional = userRepository.findByEmail(newCourse.getInstructorEmail());
        
        if(!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("instructor", "Instrutor não encontrado."));
        }
        
        User user = userOptional.get();

        if(!user.getRole().equals(INSTRUCTOR)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorItemDTO("role", "Apenas usuários instrutores podem ser autores de cursos."));
        }

        Course course = new Course(newCourse.getCode(), newCourse.getName(), user, newCourse.getDescription());
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/course/{code}/inactive")
    public ResponseEntity createCourse(@PathVariable("code") String courseCode) {

        if(!courseRepository.existsByCode(courseCode)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(new ErrorItemDTO("code", "Código do curso informado no sistema."));
       }
       Course course = courseRepository.findByCode(courseCode).get();
       course.setStatus(INACTIVE);
       course.setInactivationDate(LocalDateTime.now());
       courseRepository.save(course);

        return ResponseEntity.ok().build();
    }

}
