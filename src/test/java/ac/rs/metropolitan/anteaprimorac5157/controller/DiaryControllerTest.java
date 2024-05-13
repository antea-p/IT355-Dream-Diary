package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.entity.Tag;
import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetails;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import ac.rs.metropolitan.anteaprimorac5157.service.EmotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DiaryControllerTest {

    private DiaryEntryService mockDiaryEntryService;
    private EmotionService mockEmotionService;
    private DiaryController diaryController;
    private Model mockModel;

    private static final DiaryUserDetails USER_DETAILS = new DiaryUserDetails(new DiaryUser(
            1, "omori", "password", false));
    private static final String TITLE = "Test title";
    private static final String CONTENT = "Test content";
    private static final List<Emotion> EMOTION_LIST = List.of(new Emotion(1, "Nostalgia"),
            new Emotion(2, "Wonder"), new Emotion(3, "Joy"));


    @BeforeEach
    void setUp() {
        this.mockDiaryEntryService = mock(DiaryEntryService.class);
        this.mockEmotionService = mock(EmotionService.class);
        this.diaryController = new DiaryController(mockDiaryEntryService, mockEmotionService);
        this.mockModel = mock(Model.class);

        when(mockEmotionService.findAllEmotions()).thenReturn(EMOTION_LIST);
    }

    private void prepareTagsParameter(Map<String, String> parameters, String tags) {
        if (tags != null) {
            parameters.put("tags", tags);
        }
    }

    @Test
    void testShowCreateForm() {
        assertThat(diaryController.showCreateForm(mockModel)).isEqualTo("create");

        verify(mockModel).addAttribute("diaryEntry", new DiaryEntry());
        verify(mockModel).addAttribute("emotions", EMOTION_LIST);

    }

    @Test
    void testSaveDiaryEntryNoEmotions() {
        Map<String, String> PARAMETERS = new HashMap<>();

        PARAMETERS.put("title", TITLE);
        PARAMETERS.put("content", CONTENT);
        PARAMETERS.put("tags", "");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(PARAMETERS,USER_DETAILS)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntryOneEmotion() {
        Map<String, String> PARAMETERS = new HashMap<>();

        PARAMETERS.put("title", TITLE);
        PARAMETERS.put("content", CONTENT);
        PARAMETERS.put("tags", "");
        PARAMETERS.put("emotion_1", "1");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), List.of(new Emotion(1, "Nostalgia")));
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(PARAMETERS, USER_DETAILS)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntryAllEmotions() {
        Map<String, String> PARAMETERS = new HashMap<>();

        PARAMETERS.put("title", TITLE);
        PARAMETERS.put("content", CONTENT);
        PARAMETERS.put("tags", "");

        for (Emotion emotion : EMOTION_LIST) {
            PARAMETERS.put("emotion_ " + emotion.getId(), emotion.getId().toString());
        }

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(), EMOTION_LIST);
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(PARAMETERS, USER_DETAILS)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntryNoTags() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        prepareTagsParameter(parameters, "");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(), List.of(), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntryOneTag() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        prepareTagsParameter(parameters, "Library");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Library")), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

    @Test
    void testSaveDiaryEntryMultipleTags() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("title", TITLE);
        parameters.put("content", CONTENT);
        prepareTagsParameter(parameters, "Library, Magic, Childhood");

        DiaryEntry expectedDiaryEntry = new DiaryEntry(TITLE, CONTENT, LocalDate.now(), USER_DETAILS.getId(),
                List.of(new Tag("Library"), new Tag("Magic"), new Tag("Childhood")), List.of());
        when(mockDiaryEntryService.save(expectedDiaryEntry)).thenReturn(expectedDiaryEntry);

        assertThat(diaryController.saveDiaryEntry(parameters, USER_DETAILS)).isEqualTo("redirect:/diary");
        verify(mockDiaryEntryService, times(1)).save(expectedDiaryEntry);
    }

}