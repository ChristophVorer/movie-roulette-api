package de.vorer.movieroulette.authentication;

import de.vorer.movieroulette.authentication.dto.AuthenticationResponse;
import de.vorer.movieroulette.authentication.dto.LoginRequest;
import de.vorer.movieroulette.authentication.dto.RegisterRequest;
import de.vorer.movieroulette.authentication.exceptions.InvalidCredentialsException;
import de.vorer.movieroulette.security.JwtService;
import de.vorer.movieroulette.user.User;
import de.vorer.movieroulette.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest){
        User user = userService.register(registerRequest);

        String accessToken = jwtService.generateToken(user);

        return new AuthenticationResponse(accessToken);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        User user = userService.findByEmail(loginRequest.email())
                .orElseThrow(InvalidCredentialsException::new);

        if(!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateToken(user);

        return new AuthenticationResponse(accessToken);
    }
}
