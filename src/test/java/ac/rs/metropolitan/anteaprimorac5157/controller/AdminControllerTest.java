package ac.rs.metropolitan.anteaprimorac5157.controller;

import ac.rs.metropolitan.anteaprimorac5157.security.DiaryUserDetailsService;
import ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntryService;
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
    private Model model;

    @BeforeEach
    void setUp() {
        mockDiaryEntryService = mock(DiaryEntryService.class);
        mockDiaryUserDetailsService = mock(DiaryUserDetailsService.class);
        adminController = new AdminController(mockDiaryEntryService, mockDiaryUserDetailsService);
        model = mock(Model.class);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowAdminPage() {
        when(mockDiaryUserDetailsService.getAllUsernames()).thenReturn(List.of("User1", "User2", "User3"));
        when(mockDiaryEntryService.getDiaryEntryCountByUser()).thenReturn(Map.of("User1", 5, "User2", 3, "User3", 7));

        assertThat(adminController.showAdminPage(model)).isEqualTo("admin");
        verify(model).addAttribute("users", List.of("User1", "User2", "User3"));
        verify(model).addAttribute("statistics", Map.of("User1", 5, "User2", 3, "User3", 7));
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
}
