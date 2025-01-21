package br.com.alura.ProjetoAlura.registration;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.course.CourseRepository;
import br.com.alura.ProjetoAlura.course.Status;
import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationRepository registrationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createRegistration__should_return_bad_request_when_student_not_found() throws Exception {
        NewRegistrationDTO newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail("student@test.com");
        newRegistration.setCourseCode("spring");

        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("E-mail do estudante não encontrado no sistema"));
    }

    @Test
    void createRegistration__should_return_bad_request_when_course_not_found() throws Exception {
        NewRegistrationDTO newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail("student@test.com");
        newRegistration.setCourseCode("spring");

        User user = new User("Student", "student@test.com", Role.STUDENT, "password");
        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(Optional.of(user));
        when(courseRepository.findByCode(newRegistration.getCourseCode())).thenReturn(Optional.empty());

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRegistration)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Código do curso não encontrado no sistema"));
    }

    @Test
    void createRegistration__should_return_bad_request_when_course_inactive() throws Exception {
        NewRegistrationDTO newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail("student@test.com");
        newRegistration.setCourseCode("spring");

        User user = new User("Student", "student@test.com", Role.STUDENT, "password");
        Course course = new Course("spring", "Spring Boot Avançado", user, "Descrição do curso");
        course.setStatus(Status.INACTIVE);

        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(Optional.of(user));
        when(courseRepository.findByCode(newRegistration.getCourseCode())).thenReturn(Optional.of(course));

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((objectMapper.writeValueAsString(newRegistration))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("course"))
                .andExpect(jsonPath("$.message").value("Só é permitido matrícula em cursos ativos."));
    }

    @Test
    void createRegistration__should_return_bad_request_when_student_already_registered() throws Exception {
        NewRegistrationDTO newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail("student@test.com");
        newRegistration.setCourseCode("spring");

        User user = new User("Student", "student@test.com", Role.STUDENT, "password");
        Course course = new Course("spring", "Spring Boot Avançado", user, "Descrição do curso");

        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(Optional.of(user));
        when(courseRepository.findByCode(newRegistration.getCourseCode())).thenReturn(Optional.of(course));
        when(registrationRepository.existsByUserAndCourse(user, course)).thenReturn(true);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((objectMapper.writeValueAsString(newRegistration))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("E-mail do estudante já cadastrado no curso."));
    }

    @Test
    void report_should_return_course_report() throws Exception {
        List<Map<String, Object>> mockResults = List.of(
            Map.of(
                "course_name", "Spring Boot Avançado",
                "course_code", "spring",
                "instructor_name", "João",
                "instructor_email", "joao@alura.com",
                "total_registrations", 100
            ),
            Map.of(
                "course_name", "Java Básico",
                "course_code", "java",
                "instructor_name", "Carlos",
                "instructor_email", "carlos@alura.com",
                "total_registrations", 50
            )
        );

        when(registrationRepository.getMostAccessedCourses()).thenReturn(mockResults);

        mockMvc.perform(get("/registration/report")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Spring Boot Avançado"))
                .andExpect(jsonPath("$[0].courseCode").value("spring"))
                .andExpect(jsonPath("$[0].instructorName").value("João"))
                .andExpect(jsonPath("$[0].instructorEmail").value("joao@alura.com"))
                .andExpect(jsonPath("$[0].totalRegistrations").value(100))
                .andExpect(jsonPath("$[1].courseName").value("Java Básico"))
                .andExpect(jsonPath("$[1].courseCode").value("java"))
                .andExpect(jsonPath("$[1].instructorName").value("Carlos"))
                .andExpect(jsonPath("$[1].instructorEmail").value("carlos@alura.com"))
                .andExpect(jsonPath("$[1].totalRegistrations").value(50));
    }

    @Test
    void createRegistration__should_return_created_when_registration_is_valid() throws Exception {
        NewRegistrationDTO newRegistration = new NewRegistrationDTO();
        newRegistration.setStudentEmail("student@test.com");
        newRegistration.setCourseCode("spring");

        User user = new User("Student", "student@test.com", Role.STUDENT, "password");
        Course course = new Course("spring", "Spring Boot Avançado", user, "Descrição do curso");

        when(userRepository.findByEmail(newRegistration.getStudentEmail())).thenReturn(Optional.of(user));
        when(courseRepository.findByCode(newRegistration.getCourseCode())).thenReturn(Optional.of(course));
        when(registrationRepository.existsByUserAndCourse(user, course)).thenReturn(false);

        mockMvc.perform(post("/registration/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((objectMapper.writeValueAsString(newRegistration))))
                .andExpect(status().isCreated());
    }
}
