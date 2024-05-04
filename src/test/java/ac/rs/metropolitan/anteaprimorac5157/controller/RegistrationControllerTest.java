package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.RegistrationCommand;
import ac.rs.metropolitan.anteaprimorac5157.exception.RegistrationFailedException;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import ac.rs.metropolitan.anteaprimorac5157.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    private RegistrationController registrationController;
    private RegistrationService mockService;
    private RegistrationCommand registrationCommand;
    private Model mockModel;
    private HttpServletRequest mockRequest;
    private HttpSession mockSession;

    @BeforeEach
    void setUp() {
        this.mockService = mock(RegistrationService.class);
        this.registrationController = new RegistrationController(mockService);
        this.mockModel = mock(Model.class);
        this.mockRequest = mock(HttpServletRequest.class);
        this.mockSession = mock(HttpSession.class);
    }

    @Test
    void testRegistrationSuccess() throws RegistrationFailedException {
        registrationCommand = new RegistrationCommand("sonic", "sonic-adventures-1998", "sonic-adventures-1998");
        DiaryUserDetails expectedUserDetails = new DiaryUserDetails(new DiaryUser(98, "sonic", "some-cool-hash", false));
        when(mockService.register(registrationCommand)).thenReturn(expectedUserDetails);
        when(mockRequest.getSession(true)).thenReturn(mockSession);

        assertThat(registrationController.register(mockModel, registrationCommand, mockRequest)).isEqualTo("redirect:/");
        verify(mockSession).setAttribute(eq("SPRING_SECURITY_CONTEXT"), any());
    }

    @Test
    void testRegistrationFailsIfPasswordsDontMatch() throws RegistrationFailedException {
        registrationCommand = new RegistrationCommand("sonic", "sonic-adventures-1998", "sonic-adventures-1998-whoops");
        when(mockService.register(registrationCommand)).thenThrow(new RegistrationFailedException("Passwords don't match!"));

        assertThat(registrationController.register(mockModel, registrationCommand, mockRequest)).isEqualTo("register");
        verify(mockModel).addAttribute("error", "Passwords don't match!");
    }

    @Test
    void testRegistrationFailsIfUserExists() throws RegistrationFailedException {
        registrationCommand = new RegistrationCommand("omori", "password", "password");
        when(mockService.register(registrationCommand)).thenThrow(new RegistrationFailedException("User already exists!"));

        assertThat(registrationController.register(mockModel, registrationCommand, mockRequest)).isEqualTo("register");
        verify(mockModel).addAttribute("error", "User already exists!");
    }
}