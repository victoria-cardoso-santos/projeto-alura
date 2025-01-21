package br.com.alura.ProjetoAlura.registration;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import br.com.alura.ProjetoAlura.util.ErrorItemDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RegistrationController {
    private RegistrationRepository registrationRepository;
    private UserRepository userRepository;
    private CourseRepository courseRepository;

    public RegistrationController(RegistrationRepository registrationRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.registrationRepository = registrationRepository;
    }

    @PostMapping("/registration/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewRegistrationDTO newRegistration) {

        Optional<User> userOptional = userRepository.findByEmail(newRegistration.getStudentEmail());
        Optional<Course> courseOptional = courseRepository.findByCode(newRegistration.getCourseCode());

        if(!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "E-mail do estudante não encontrado no sistema"));
        }
        
        if(!courseOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("code", "Código do curso não encontrado no sistema"));
        }

        if(courseOptional.get().getStatus() == Status.INACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("course", "Só é permitido matrícula em cursos ativos."));
        }

        if(registrationRepository.existsByUserAndCourse(userOptional.get(),courseOptional.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "E-mail do estudante já cadastrado no curso."));
        }


        Registration registration = new Registration(userOptional.get(), courseOptional.get());
        registrationRepository.save(registration);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<Map<String, Object>> results = registrationRepository.getMostAccessedCourses();
    
        List<RegistrationReportItem> reportItems = results.stream().map(row -> 
            new RegistrationReportItem(
                (String) row.get("course_name"), 
                (String) row.get("course_code"), 
                (String) row.get("instructor_name"), 
                (String) row.get("instructor_email"), 
                ((Number) row.get("total_registrations")).longValue()
            )
        ).toList();
    
        return ResponseEntity.ok(reportItems);
    }

}
