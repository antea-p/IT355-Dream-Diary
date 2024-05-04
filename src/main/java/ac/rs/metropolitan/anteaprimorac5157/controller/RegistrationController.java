package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.RegistrationCommand;
import ac.rs.metropolitan.anteaprimorac5157.exception.RegistrationFailedException;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import ac.rs.metropolitan.anteaprimorac5157.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping()
    public String showRegister() {
        return "register";
    }

    @PostMapping()
    public String register(Model model, @ModelAttribute RegistrationCommand registrationCommand) {
        // In the POST endpoint, call RegistrationService.register.
        // On a success, return “redirect:/”.
        // On a failure, set the exception message as the error attribute on the model,
        // and return the register view again.
        try {
            registrationService.register(registrationCommand);
            return "redirect:/";
        } catch (RegistrationFailedException e) {
            model.addAttribute("error", e.getMessage());
            return "register";

        }
    }
}
