package de.vorer.movieroulette.authentication.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    private Set<ConstraintViolation<RegisterRequest>> validatePassword(String password) {
        return validator.validate(new RegisterRequest("christoph", "christoph@example.com", password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "MeinSicheresPasswort123!",
            "Abcdef1!",
            "Übergröße9#",
            "Pass wort1!"
    })
    void acceptsPasswordsMatchingThePolicy(String password) {
        assertThat(validatePassword(password)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "meinpasswort123!",
            "MEINPASSWORT123!",
            "MeinPasswort!",
            "MeinPasswort123",
            "MeinPasswort 123"
    })
    void rejectsPasswordsMissingACharacterClass(String password) {
        assertThat(validatePassword(password))
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("Das Passwort muss Groß- und Kleinbuchstaben, eine Zahl und ein Sonderzeichen enthalten.");
    }

    @Test
    void rejectsTooShortPassword() {
        assertThat(validatePassword("Ab1!xyz"))
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("Das Passwort muss zwischen 8 und 64 Zeichen lang sein.");
    }

    @Test
    void rejectsTooLongPassword() {
        String password = "Aa1!" + "x".repeat(61);

        assertThat(validatePassword(password))
                .extracting(ConstraintViolation::getMessage)
                .containsExactly("Das Passwort muss zwischen 8 und 64 Zeichen lang sein.");
    }
}