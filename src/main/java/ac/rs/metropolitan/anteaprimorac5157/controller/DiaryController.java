package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.entity.Tag;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import ac.rs.metropolitan.anteaprimorac5157.service.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryEntryService diaryEntryService;
    private final EmotionService emotionService;

    @Autowired
    public DiaryController(DiaryEntryService diaryEntryService, EmotionService emotionService) {
        this.diaryEntryService = diaryEntryService;
        this.emotionService = emotionService;
    }

    // TODO: search & sort
    @GetMapping
    public String showDiaryList(Model model) {
        model.addAttribute("diaryEntries", diaryEntryService.list());
        return "list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("diaryEntry", new DiaryEntry());
        model.addAttribute("emotions", emotionService.findAllEmotions());
        return "create";
    }

    // TODO: refactoring
    @PostMapping("/create")
    public String saveDiaryEntry(@RequestParam Map<String,String> parameters,
                                 @AuthenticationPrincipal DiaryUserDetails currentUser) {
        System.out.println(parameters);

        String title = parameters.get("title");
        String content = parameters.get("content");
        String tagsInput = parameters.get("tags");

        List<Tag> tags = Arrays.stream(tagsInput.split(","))
                .map(String::trim) // Briše suvišne razmake (blanko)
                .filter(tag -> !tag.isEmpty())
                .map(tag -> new Tag(tag, null))  // Privremeni null jer još nije kreiran DiaryEntry objekat
                .collect(Collectors.toList());

        List<Emotion> selectedEmotions = extractSelectedEmotions(parameters);

        DiaryEntry diaryEntry = new DiaryEntry()
                .setTitle(title)
                .setContent(content)
                .setCreatedDate(LocalDate.now())
                .setUserId(currentUser.getId())
                .setEmotions(selectedEmotions)
                .setTags(tags);

        tags.forEach(tag -> tag.setEntry(diaryEntry));  // Sada se podešava korektni DiaryEntry objekat
        diaryEntryService.save(diaryEntry);

        return "redirect:/diary";
    }

    private List<Emotion> extractSelectedEmotions(Map<String, String> parameters) {
        // 1. Dohvaća listu svih emocija putem servisa.
        // 2. Zatim ekstrahira ID-jeve emocija koje je korisnik čekirao prilikom podnošenja forme
        // 3. Filtrira se lista svih emocija kako bi se dobilo konačnu listu Emotion objekata, za selektirane emocije.
        List<Emotion> allEmotions = emotionService.findAllEmotions();
        List<Integer> selectedEmotionIds = parameters.keySet().stream()
                .filter(key -> key.startsWith("emotion_"))
                .map(key -> Integer.parseInt(parameters.get(key)))
                .toList();
        List<Emotion> selectedEmotions = allEmotions.stream()
                .filter(emotion -> selectedEmotionIds.contains(emotion.getId()))
                .toList();
        return selectedEmotions;
    }

    // TODO: edit mapiranje

    @GetMapping("/delete/{id}")
    public String deleteDiaryEntry(@PathVariable Integer id) {
        diaryEntryService.delete(id);
        return "redirect:/diary";
    }


    @GetMapping("/show/{id}")
    public String showDiaryEntry(@PathVariable("id") Integer id, Model model) {
        DiaryEntry diaryEntry = diaryEntryService.get(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid diaryEntry Id: " + id));
        model.addAttribute("diaryEntry", diaryEntry);
        return "show";
    }
}
