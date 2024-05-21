package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetailsService;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final DiaryEntryService diaryEntryService;
    private final DiaryUserDetailsService diaryUserDetailsService;

    public AdminController(DiaryEntryService diaryEntryService, DiaryUserDetailsService diaryUserDetailsService) {
        this.diaryEntryService = diaryEntryService;
        this.diaryUserDetailsService = diaryUserDetailsService;
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", diaryUserDetailsService.getAllUsernames());
        model.addAttribute("statistics", diaryEntryService.getDiaryEntryCountByUser());
        return "admin";
    }

}
