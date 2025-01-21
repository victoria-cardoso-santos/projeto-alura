package br.com.alura.ProjetoAlura.registration;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class NewRegistrationDTO {

    @NotNull
    @NotBlank
    @Length(min = 4, max = 10)
    @Pattern(regexp = "^[a-zA-Z-]+$", message = "O código deve conter apenas letras e hífens, sem espaços, números ou outros caracteres.")
    private String courseCode;

    @NotBlank
    @NotNull
    @Email
    private String studentEmail;

    public NewRegistrationDTO() {}

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

}
