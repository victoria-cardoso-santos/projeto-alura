package br.com.alura.ProjetoAlura.course;

import java.time.LocalDate;

import br.com.alura.ProjetoAlura.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;

    private String name;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "inactivation_date")
    private LocalDate inactivationDate;

    public Course(String code, String name, User instructor, String description) {
        this.code = code;
        this.name = name;
        this.instructor = instructor;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getInactivationDate() {
        return inactivationDate;
    }

    public User getInstructor() {
        return instructor;
    }

    public Status getStatus() {
        return status;
    }

}
