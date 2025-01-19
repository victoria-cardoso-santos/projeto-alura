package br.com.alura.ProjetoAlura.course;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class NewCourseDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Length(min = 4, max = 10)
    @Pattern(regexp = "^[a-zA-Z-]+$", message = "O código deve conter apenas letras e hífens, sem espaços, números ou outros caracteres.")
    private String code;

    private String description;

    @NotNull
    @NotBlank
    @Email
    private String instructorEmail;

    public NewCourseDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

   
}
