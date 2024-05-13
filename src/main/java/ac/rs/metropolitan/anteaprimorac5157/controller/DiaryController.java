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

    @PostMapping("/create")
    public String saveDiaryEntry(@RequestParam Map<String,String> parameters,
                                 @AuthenticationPrincipal DiaryUserDetails currentUser) {
        DiaryEntry diaryEntry = createDiaryEntryObject(parameters, currentUser);
        diaryEntry.setCreatedDate(LocalDate.now());
        diaryEntryService.save(diaryEntry);

        return "redirect:/diary";
    }

@GetMapping("/edit/{id}")
public String showEditForm(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal DiaryUserDetails currentUser) {
    DiaryEntry diaryEntry = diaryEntryService.get(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid diary Id:" + id));
    if (currentUser.getId().equals(diaryEntry.getUserId())) {
        List<Emotion> allEmotions = emotionService.findAllEmotions();
        List<Integer> selectedEmotionIds = diaryEntry.getEmotions().stream()
                .map(Emotion::getId)
                .collect(Collectors.toList());

        model.addAttribute("diaryEntry", diaryEntry);
        model.addAttribute("allEmotions", allEmotions);
        model.addAttribute("selectedEmotionIds", selectedEmotionIds);

        String commaSeparatedTags = diaryEntry.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));
        model.addAttribute("commaSeparatedTags", commaSeparatedTags);

        return "edit";
    }

    return "login";
}

    @PostMapping("/edit")
    public String updateDiaryEntry(@RequestParam Map<String, String> parameters,
                                   @AuthenticationPrincipal DiaryUserDetails currentUser) {
        System.out.println("PARAMETERS: " + parameters);
        DiaryEntry oldDiaryEntry = diaryEntryService.get(Integer.valueOf(parameters.get("id"))).orElseThrow();
        if (currentUser.getId().equals(oldDiaryEntry.getUserId())) {
            DiaryEntry updatedDiaryEntry = createDiaryEntryObject(parameters, currentUser);
            updatedDiaryEntry.setId(oldDiaryEntry.getId());
            updatedDiaryEntry.setCreatedDate(oldDiaryEntry.getCreatedDate());

            System.out.println("UPDATED DIARYENTRY:" + updatedDiaryEntry);

            diaryEntryService.save(updatedDiaryEntry);

            return "redirect:/diary";
        }

        return "redirect:/login";
    }

    private DiaryEntry createDiaryEntryObject(Map<String, String> parameters, DiaryUserDetails currentUser) {
        String title = parameters.get("title");
        String content = parameters.get("content");
        String tagsInput = parameters.get("tags");

        List<Tag> tags = extractTags(tagsInput);

        List<Emotion> selectedEmotions = extractSelectedEmotions(parameters);

        DiaryEntry diaryEntry = new DiaryEntry()
                .setTitle(title)
                .setContent(content)
                .setUserId(currentUser.getId())
                .setEmotions(selectedEmotions)
                .setTags(tags);
        return diaryEntry;
    }

    private static List<Tag> extractTags(String tagsInput) {
        return Arrays.stream(tagsInput.split(","))
                .map(String::trim) // Briše suvišne razmake (blanko)
                .filter(tag -> !tag.isEmpty())
                .map(Tag::new)
                .collect(Collectors.toList());
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
