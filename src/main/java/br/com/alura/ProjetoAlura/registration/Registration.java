package br.com.alura.ProjetoAlura.registration;

import java.time.LocalDateTime;

import br.com.alura.ProjetoAlura.course.Course;
import br.com.alura.ProjetoAlura.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name="registration_date", nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    public Registration() {

    }

    public Registration(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public User getUser() {
        return user;
    }

}
