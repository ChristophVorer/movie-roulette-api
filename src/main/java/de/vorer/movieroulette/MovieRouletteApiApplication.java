package de.vorer.movieroulette;

import de.vorer.movieroulette.user.User;
import de.vorer.movieroulette.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MovieRouletteApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieRouletteApiApplication.class, args);
    }
}
