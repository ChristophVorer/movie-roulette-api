package de.vorer.movieroulette.user;

import de.vorer.movieroulette.authentication.dto.RegisterRequest;
import de.vorer.movieroulette.user.exception.EmailAlreadyExistsException;
import de.vorer.movieroulette.user.exception.UsernameAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest registerRequest){

        if(userRepository.existsByEmail(registerRequest.email())){
            throw new EmailAlreadyExistsException("Ein Nutzer mit dieser Email existiert bereits");
        }

        if(userRepository.existsByUsername(registerRequest.username())){
            throw new UsernameAlreadyExistsException("Ein Nutzer mit diesem Usernamen existiert bereits");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
