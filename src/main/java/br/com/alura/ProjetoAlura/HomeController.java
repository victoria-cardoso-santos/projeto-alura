package br.com.alura.ProjetoAlura;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return """
                <h1>Projeto Alura</h1>
                <p>Bem-vinda ao teste para <b>Pessoa Desenvolvedora Java</b> da Alura!</p>
            """;
    }
}
