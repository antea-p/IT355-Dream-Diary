package ac.rs.metropolitan.anteaprimorac5157.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest {

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        this.loginController = new LoginController();
    }

    @Test
    void testShowLoginForm() {
        assertThat(loginController.showLogin()).isEqualTo("login");
    }
}