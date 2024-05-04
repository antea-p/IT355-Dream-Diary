package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.RegistrationCommand;
import ac.rs.metropolitan.anteaprimorac5157.exception.RegistrationFailedException;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import ac.rs.metropolitan.anteaprimorac5157.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String register(Model model, @ModelAttribute RegistrationCommand registrationCommand, HttpServletRequest request) {
        try {
            UserDetails registeredUserDetails = registrationService.register(registrationCommand);
            // Dohvaća novog korisnika i stavlja ga u tekuću sesiju (da bi se tretirao kao logiran korisnik)
            Authentication auth = new UsernamePasswordAuthenticationToken(registeredUserDetails,
                    null, registeredUserDetails.getAuthorities());

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(auth);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            return "redirect:/";
        } catch (RegistrationFailedException e) {
            model.addAttribute("error", e.getMessage());
            return "register";

        }
    }
}
