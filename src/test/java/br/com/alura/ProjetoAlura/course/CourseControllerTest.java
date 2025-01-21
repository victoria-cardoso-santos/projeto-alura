package br.com.alura.ProjetoAlura.course;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.ProjetoAlura.user.Role;
import br.com.alura.ProjetoAlura.user.User;
import br.com.alura.ProjetoAlura.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCourse__should_return_bad_request_when_code_already_exists() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("spring");
        newCourseDTO.setName("Spring Boot Avançado");
        newCourseDTO.setInstructorEmail("instructor@test.com");
        newCourseDTO.setDescription("Aprenda Spring Boot avançado.");

        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(true);

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("code"))
                .andExpect(jsonPath("$.message").value("Código do curso já cadastrado no sistema."));
    }

    @Test
    void createCourse__should_return_bad_request_when_instructor_not_found() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("spring");
        newCourseDTO.setName("Spring Boot Avançado");
        newCourseDTO.setInstructorEmail("instructor@test.com");
        newCourseDTO.setDescription("Aprenda Spring Boot avançado");

        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(false);
        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("instructor"))
                .andExpect(jsonPath("$.message").value("Instrutor não encontrado."));
    }

    @Test
    void createCourse__should_return_bad_request_when_user_is_not_instructor() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("spring");
        newCourseDTO.setName("Spring Boot Avançado");
        newCourseDTO.setInstructorEmail("instructor@test.com");
        newCourseDTO.setDescription("Aprenda Spring Boot avançado");

        User user = new User("Student", "instructor@test.com", Role.STUDENT, "password");
        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(false);
        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("role"))
                .andExpect(jsonPath("$.message").value("Apenas usuários instrutores podem ser autores de cursos."));
    }
    @Test
    void createCourse__should_return_created_when_request_is_valid() throws Exception {
        NewCourseDTO newCourseDTO = new NewCourseDTO();
        newCourseDTO.setCode("spring");
        newCourseDTO.setName("Spring Boot Avançado");
        newCourseDTO.setInstructorEmail("instructor@test.com");
        newCourseDTO.setDescription("Aprenda Spring Boot avançado");

        User user = new User("Instructor", "instructor@test.com", Role.INSTRUCTOR, "password");
        when(courseRepository.existsByCode(newCourseDTO.getCode())).thenReturn(false);
        when(userRepository.findByEmail(newCourseDTO.getInstructorEmail())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/course/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseDTO)))
                .andExpect(status().isCreated());
    }
}
