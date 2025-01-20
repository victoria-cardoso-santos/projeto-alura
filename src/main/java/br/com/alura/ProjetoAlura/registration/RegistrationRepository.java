package br.com.alura.ProjetoAlura.registration;

import org.springframework.stereotype.Repository;

import br.com.alura.ProjetoAlura.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
   boolean existsByUser(User user) ;
}
