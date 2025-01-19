package br.com.alura.ProjetoAlura.user;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static br.com.alura.ProjetoAlura.user.Role.INSTRUCTOR;

public class NewInstructorUserDTO {
    @NotNull
    @Length(min = 3, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Length(min = 8, max = 16)
    private String password;

    public NewInstructorUserDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User toModel() {
        return new User(name, email, INSTRUCTOR, password);
    }
}
