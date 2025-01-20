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

import java.util.ArrayList;
import java.util.List;
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
        // TODO: Implementar a Questão 3 - Criação de Matrículas aqui...

        Optional<User> userOptional = userRepository.findByEmail(newRegistration.getStudentEmail());
        Optional<Course> courseOptional = courseRepository.findByCode(newRegistration.getCourseCode());

        if(!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "E-mail do estudante não encontrado no sistema"));
        }
        if(registrationRepository.existsByUser(userOptional.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("email", "E-mail do estudante já cadastrado no curso."));
        }

        if(!courseOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("código", "Código do curso não encontrado no sistema"));
        }
        if(courseOptional.get().getStatus() == Status.INACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("curso", "Só é permitido matrícula em cursos ativos."));
        }

        Registration registration = new Registration(userOptional.get(), courseOptional.get());
        registrationRepository.save(registration);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/registration/report")
    public ResponseEntity<List<RegistrationReportItem>> report() {
        List<RegistrationReportItem> items = new ArrayList<>();

        // TODO: Implementar a Questão 4 - Relatório de Cursos Mais Acessados aqui...

        // Dados fictícios abaixo que devem ser substituídos
        items.add(new RegistrationReportItem(
                "Java para Iniciantes",
                "java",
                "Charles",
                "charles@alura.com.br",
                10L
        ));

        items.add(new RegistrationReportItem(
                "Spring para Iniciantes",
                "spring",
                "Charles",
                "charles@alura.com.br",
                9L
        ));

        items.add(new RegistrationReportItem(
                "Maven para Avançados",
                "maven",
                "Charles",
                "charles@alura.com.br",
                9L
        ));

        return ResponseEntity.ok(items);
    }

}
