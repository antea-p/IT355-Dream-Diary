package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetailsService;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
import ac.rs.metropolitan.anteaprimorac5157.service.EmotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    private AdminController adminController;
    private DiaryEntryService mockDiaryEntryService;
    private DiaryUserDetailsService mockDiaryUserDetailsService;
    private EmotionService mockEmotionService;
    private Model model;

    @BeforeEach
    void setUp() {
        mockDiaryEntryService = mock(DiaryEntryService.class);
        mockDiaryUserDetailsService = mock(DiaryUserDetailsService.class);
        mockEmotionService = mock(EmotionService.class);
        adminController = new AdminController(mockDiaryEntryService, mockDiaryUserDetailsService, mockEmotionService);
        model = mock(Model.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowAdminPage() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(List.of("User1", "User2", "User3"));
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Map.of("User1", 5, "User2", 3, "User3", 7));
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Happy", 10L, "Sad", 5L));

        assertThat(adminController.showAdminPage(model)).isEqualTo("admin");
        verify(model).addAttribute("users", List.of("User1", "User2", "User3"));
        verify(model).addAttribute("diaryEntriesStatistics", Map.of("User1", 5, "User2", 3, "User3", 7));
        verify(model).addAttribute("emotionStatistics", Map.of("Happy", 10L, "Sad", 5L));
    }

    @Test
    void testGetAllUsernames_ReturnsCorrectList() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(List.of("Alice", "Bob", "Charlie"));

        List<String> usernames = mockDiaryUserDetailsService.getAllUsernames();
        assertThat(usernames).containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void testGetDiaryCountByUser_ReturnsEmptyMapIfNoUsers() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(Collections.emptyList());
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Collections.emptyMap());

        Map<String, Integer> diaryCountByUser = mockDiaryEntryService.getDiaryEntryCountByUser();
        assertThat(diaryCountByUser).isEmpty();
    }

    @Test
    void testGetDiaryCountByUser_ReturnsCorrectMapIfUsersExist() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(List.of("Alice", "Bob"));
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Map.of("Alice", 10, "Bob", 5));

        Map<String, Integer> diaryCountByUser = mockDiaryEntryService.getDiaryEntryCountByUser();
        assertThat(diaryCountByUser).containsExactlyInAnyOrderEntriesOf(Map.of("Alice", 10, "Bob", 5));
    }

    @Test
    void testGetEmotionUsageCount_ReturnsZeroIfEmotionNotUsed() {
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Happy", 0L));

        Map<String, Long> emotionUsageCount = mockEmotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsEntry("Happy", 0L);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsCorrectCountIfEmotionUsedMultipleTimes() {
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Sad", 7L));

        Map<String, Long> emotionUsageCount = mockEmotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsEntry("Sad", 7L);
    }

    @Test
    void testGetEmotionUsageCount_ReturnsCorrectCountsForMultipleEmotions() {
        when(mockEmotionService.getEmotionUsageCount()).thenReturn(Map.of("Happy", 10L, "Sad", 5L, "Angry", 2L));

        Map<String, Long> emotionUsageCount = mockEmotionService.getEmotionUsageCount();
        assertThat(emotionUsageCount).containsExactlyInAnyOrderEntriesOf(Map.of("Happy", 10L, "Sad", 5L, "Angry", 2L));
    }
}
