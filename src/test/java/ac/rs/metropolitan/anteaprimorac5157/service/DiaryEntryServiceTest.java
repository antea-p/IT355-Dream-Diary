package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.*;
import ac.rs.metropolitan.anteaprimorac5157.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ac.rs.metropolitan.anteaprimorac5157.service.DiaryEntrySortingCriteria.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DiaryEntryServiceTest {

    private DiaryEntryRepository mockDiaryEntryRepository;
    private DiaryUserRepository mockDiaryUserRepository;
    private DiaryEntryEmotionRepository mockDiaryEntryEmotionRepository;
    private EmotionRepository mockEmotionRepository;
    private DiaryEntryService diaryEntryService;


    public static final Integer VALID_ID = 1;
    private final static DiaryEntry DIARY_ENTRY = new DiaryEntry(VALID_ID, "Welcome to White Space", "I have been here as long as I can remember...",
            LocalDate.now(), 1, List.of(), List.of(new Emotion(1, "Nostalgia")));
    private final static DiaryEntry DIARY_ENTRY_2 = new DiaryEntry(2, "Exploring Neighbor's Room", "She has a wide array of books and colorful knick-knacks...",
            LocalDate.now(), 2, List.of(new Tag(1, "Library")), List.of(new Emotion(2, "Wonder")));

    @BeforeEach
    void setUp() {
        this.mockDiaryEntryRepository = mock(DiaryEntryRepository.class);
        this.mockDiaryUserRepository = mock(DiaryUserRepository.class);
        this.mockDiaryEntryEmotionRepository = mock(DiaryEntryEmotionRepository.class);
        this.mockEmotionRepository = mock(EmotionRepository.class);
        this.diaryEntryService = new DiaryEntryService(mockDiaryEntryRepository, mockDiaryUserRepository, mockDiaryEntryEmotionRepository, mockEmotionRepository);
    }


    @Test
    void testListAllDiaryEntries() {
        List<DiaryEntry> expectedResult = List.of(DIARY_ENTRY, DIARY_ENTRY_2);
        when(mockDiaryEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(expectedResult);

        List<DiaryEntry> actualResult = diaryEntryService.list(null, null, null);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void testFindEntriesByTitle_NoSorting() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), 1, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), 2, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of());

        List<DiaryEntry> unsortedEntries = List.of(entry1, entry2, entry3);
        List<DiaryEntry> expectedResult = List.of(entry3);
        String searchTitle = "amm";
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        when(mockDiaryEntryRepository.findByTitleContainingIgnoreCase(searchTitle, sort)).thenReturn(expectedResult);

        List<DiaryEntry> actualResult = diaryEntryService.list(null, null, searchTitle);

        assertThat(actualResult).hasSameElementsAs(expectedResult);
        verify(mockDiaryEntryRepository).findByTitleContainingIgnoreCase(searchTitle, sort);
    }

    @Test
    void testFindEntriesByTitle_WithSorting() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), 1, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), 2, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of());

        List<DiaryEntry> sortedMatchedEntries = List.of(entry3);
        String searchTitle = "amm";
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        when(mockDiaryEntryRepository.findByTitleContainingIgnoreCase(searchTitle, sort)).thenReturn(sortedMatchedEntries);

        List<DiaryEntry> actualResult = diaryEntryService.list(Sort.Direction.ASC, DiaryEntrySortingCriteria.TITLE, searchTitle);

        assertThat(actualResult).isEqualTo(sortedMatchedEntries);
        verify(mockDiaryEntryRepository).findByTitleContainingIgnoreCase(searchTitle, sort);
    }

    @Test
    void testListAllDiaryEntries_SortedByTitle_Ascending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), 1, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), 2, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry1, entry2, entry3);
        Sort sort = Sort.by(Sort.Direction.ASC, "title");
        when(mockDiaryEntryRepository.findAll(sort)).thenReturn(sortedEntries);

        List<DiaryEntry> actualResult = diaryEntryService.list(Sort.Direction.ASC, DiaryEntrySortingCriteria.TITLE, null);

        assertThat(actualResult).isEqualTo(sortedEntries);
        verify(mockDiaryEntryRepository).findAll(sort);
    }


    @Test
    void testListAllDiaryEntries_SortedByTitle_Descending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), 1, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), 2, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry3, entry2, entry1);
        when(mockDiaryEntryRepository.findAll(Sort.by(Sort.Direction.DESC, "title"))).thenReturn(sortedEntries);

        List<DiaryEntry> actualResult = diaryEntryService.list(Sort.Direction.DESC, TITLE, null);

        assertThat(actualResult).isEqualTo(sortedEntries);
    }

    @Test
    void testListAllDiaryEntries_SortedByDate_Ascending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now().minusDays(2), 1, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now().minusDays(1), 2, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry1, entry2, entry3);
        when(mockDiaryEntryRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"))).thenReturn(sortedEntries);

        List<DiaryEntry> actualResult = diaryEntryService.list(Sort.Direction.ASC, DiaryEntrySortingCriteria.CREATEDDATE, null);
        assertThat(actualResult).isEqualTo(sortedEntries);
    }

    @Test
    void testListAllDiaryEntries_SortedByDate_Descending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now().minusDays(2), 1, List.of(), List.of());
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now().minusDays(1), 2, List.of(), List.of());
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of());

        List<DiaryEntry> sortedEntries = List.of(entry3, entry2, entry1);
        when(mockDiaryEntryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"))).thenReturn(sortedEntries);

        List<DiaryEntry> actualResult = diaryEntryService.list(Sort.Direction.DESC, DiaryEntrySortingCriteria.CREATEDDATE, null);
        assertThat(actualResult).isEqualTo(sortedEntries);
    }


    @Test
    void testGetDiaryEntry_Exists() {
        when(mockDiaryEntryRepository.findById(VALID_ID)).thenReturn(Optional.of(DIARY_ENTRY));

        Optional<DiaryEntry> actualResult = diaryEntryService.get(VALID_ID);
        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult).isEqualTo(Optional.of(DIARY_ENTRY));
    }

    @Test
    void testGetDiaryEntry_DoesntExist() {
        Integer INVALID_ID = 999;
        when(mockDiaryEntryRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Optional<DiaryEntry> actualResult = diaryEntryService.get(INVALID_ID);
        assertThat(actualResult.isPresent()).isFalse();
    }

    @Test
    void testSaveDiaryEntry() {
        when(mockDiaryEntryRepository.save(DIARY_ENTRY)).thenReturn(DIARY_ENTRY);

        DiaryEntry actualResult = diaryEntryService.save(DIARY_ENTRY);
        assertThat(actualResult).isEqualTo(DIARY_ENTRY);
    }

    @Test
    void testDeleteDiaryEntry() {
        doNothing().when(mockDiaryEntryRepository).delete(DIARY_ENTRY);

       diaryEntryService.delete(VALID_ID);
        verify(mockDiaryEntryRepository, times(1)).deleteById(VALID_ID);
    }

    @Test
    void testUpdateDiaryEntryEmotions_AddNewEmotion() {
        Emotion newEmotion = new Emotion(2, "Wonder");
        List<Emotion> selectedEmotions = List.of(newEmotion);

        when(mockDiaryEntryEmotionRepository.findByEntryId(DIARY_ENTRY.getId())).thenReturn(List.of());

        diaryEntryService.updateDiaryEntryEmotions(DIARY_ENTRY, selectedEmotions);

        verify(mockDiaryEntryEmotionRepository, times(1)).save(any(DiaryEntryEmotion.class));
    }

    @Test
    void testUpdateDiaryEntryEmotions_RemoveExistingEmotion() {
        Emotion existingEmotion = new Emotion(1, "Nostalgia");
        DiaryEntryEmotion existingEntryEmotion = new DiaryEntryEmotion();
        existingEntryEmotion.setEntry(DIARY_ENTRY);
        existingEntryEmotion.setEmotion(existingEmotion);

        when(mockDiaryEntryEmotionRepository.findByEntryId(DIARY_ENTRY.getId())).thenReturn(List.of(existingEntryEmotion));

        diaryEntryService.updateDiaryEntryEmotions(DIARY_ENTRY, List.of());

        verify(mockDiaryEntryEmotionRepository, times(1)).delete(existingEntryEmotion);
    }

    @Test
    void testGetEmotionsForDiaryEntry() {
        Emotion emotion = new Emotion(1, "Nostalgia");
        DiaryEntryEmotion entryEmotion = new DiaryEntryEmotion();
        entryEmotion.setEntry(DIARY_ENTRY);
        entryEmotion.setEmotion(emotion);

        when(mockDiaryEntryEmotionRepository.findByEntryId(DIARY_ENTRY.getId())).thenReturn(List.of(entryEmotion));

        List<Emotion> emotions = diaryEntryService.getEmotionsForDiaryEntry(DIARY_ENTRY.getId());
        assertThat(emotions).containsExactly(emotion);
    }

    @Test
    void testGetDiaryEntryCountByUser() {
        DiaryUser user1 = new DiaryUser(1, "sunny", "password", false);
        DiaryUser user2 = new DiaryUser(2, "omori", "password", false);
        List<DiaryUser> users = List.of(user1, user2);

        when(mockDiaryUserRepository.findAll()).thenReturn(users);
        when(mockDiaryEntryRepository.countByUserId(1)).thenReturn(5);
        when(mockDiaryEntryRepository.countByUserId(2)).thenReturn(3);

        Map<String, Integer> diaryCountByUser = diaryEntryService.getDiaryEntryCountByUser();
        assertThat(diaryCountByUser).containsExactlyInAnyOrderEntriesOf(Map.of("sunny", 5, "omori", 3));
    }

    @Test
    void testGetTagUsageStatistics() {
        TagUsage tagUsage1 = mock(TagUsage.class);
        when(tagUsage1.getName()).thenReturn("Dream");
        when(tagUsage1.getCount()).thenReturn(15L);

        TagUsage tagUsage2 = mock(TagUsage.class);
        when(tagUsage2.getName()).thenReturn("Nightmare");
        when(tagUsage2.getCount()).thenReturn(8L);

        when(mockDiaryEntryRepository.countTagUsage()).thenReturn(List.of(tagUsage1, tagUsage2));

        List<TagUsage> tagUsageStatistics = diaryEntryService.getTagUsageStatistics();
        assertThat(tagUsageStatistics).containsExactlyInAnyOrder(tagUsage1, tagUsage2);
    }
}