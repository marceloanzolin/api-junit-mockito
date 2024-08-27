package br.com.anzolin.api.config;

import br.com.anzolin.api.domain.User;
import br.com.anzolin.api.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void startDB(){
        User u1 = new User(null, "Maria Brown" , "maria@email.com", "123456");
        User u2 = new User(null, "João" , "joao@email.com", "123");
        userRepository.saveAll(List.of(u1,u2));
    }
}
