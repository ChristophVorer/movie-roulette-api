package de.vorer.movieroulette.authentication;

import de.vorer.movieroulette.authentication.dto.AuthenticationResponse;
import de.vorer.movieroulette.authentication.dto.LoginRequest;
import de.vorer.movieroulette.authentication.exceptions.InvalidCredentialsException;
import de.vorer.movieroulette.security.JwtService;
import de.vorer.movieroulette.user.User;
import de.vorer.movieroulette.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void loginShouldReturnAccessTokenForValidCredentials() {
        User user = new User();
        user.setUsername("christoph");
        user.setPasswordHash("{bcrypt}hash");

        LoginRequest request = new LoginRequest("christoph@example.com", "password");

        when(userService.findByEmail("christoph@example.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "{bcrypt}hash")).thenReturn(true);

        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.login(request);

        assertEquals("jwt-token", response.accessToken());
    }

    @Test
    void loginShouldThrowExceptionForWrongPassword() {
        User user = new User();
        user.setEmail("christoph@example.com");
        user.setPasswordHash("{bcrypt}hash");

        LoginRequest request = new LoginRequest("christoph@example.com", "wrong-password");

        when(userService.findByEmail("christoph@example.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong-password", "{bcrypt}hash")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(request));

        verify(jwtService, never()).generateToken(user);
    }

    @Test
    void loginShouldThrowExceptionForUnknownUsername() {
        LoginRequest request = new LoginRequest("christoph@example.com", "password");

        when(userService.findByEmail("christoph@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(request));
    }
}