package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.entity.Tag;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DiaryEntryServiceTest {

    private DiaryEntryRepository mockRepository;
    private DiaryEntryService diaryEntryService;

    public static final Integer VALID_ID = 1;
    private final static DiaryEntry DIARY_ENTRY = new DiaryEntry(VALID_ID, "Welcome to White Space", "I have been here as long as I can remember...",
            LocalDate.now(), 1, List.of(), List.of(new Emotion(1, "Nostalgia")));
    private final static DiaryEntry DIARY_ENTRY_2 = new DiaryEntry(2, "Exploring Neighbor's Room", "She has a wide array of books and colorful knick-knacks...",
            LocalDate.now(), 2, List.of(new Tag(1, "Library")), List.of(new Emotion(2, "Wonder")));

    @BeforeEach
    void setUp() {
        this.mockRepository = mock(DiaryEntryRepository.class);
        this.diaryEntryService = new DiaryEntryService(mockRepository);
    }


    @Test
    void testListAllDiaryEntries() {
        List<DiaryEntry> expectedResult = List.of(DIARY_ENTRY, DIARY_ENTRY_2);
        when(mockRepository.findAll()).thenReturn(expectedResult);

        List<DiaryEntry> actualResult = diaryEntryService.list();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void testListAllDiaryEntries_SortedByTitle_Ascending() {
        DiaryEntry entry1 = new DiaryEntry(1, "Alpha", "Content 1", LocalDate.now(), 1, List.of(), List.of(new Emotion(1, "Nostalgia")));
        DiaryEntry entry2 = new DiaryEntry(2, "Beta", "Content 2", LocalDate.now(), 2, List.of(), List.of(new Emotion(2, "Wonder")));
        DiaryEntry entry3 = new DiaryEntry(3, "Gamma", "Content 3", LocalDate.now(), 3, List.of(), List.of(new Emotion(3, "Joy")));

        List<DiaryEntry> sortedEntries = List.of(entry1, entry2, entry3);
        when(mockRepository.findAllByOrderByTitleAsc()).thenReturn(sortedEntries);

        List<DiaryEntry> actualResult = diaryEntryService.listSortedByTitle();
        assertThat(actualResult).isEqualTo(sortedEntries);
    }

    @Test
    void testGetDiaryEntry_Exists() {
        when(mockRepository.findById(VALID_ID)).thenReturn(Optional.of(DIARY_ENTRY));

        Optional<DiaryEntry> actualResult = diaryEntryService.get(VALID_ID);
        assertThat(actualResult.isPresent()).isTrue();
        assertThat(actualResult).isEqualTo(Optional.of(DIARY_ENTRY));
    }

    @Test
    void testGetDiaryEntry_DoesntExist() {
        Integer INVALID_ID = 999;
        when(mockRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Optional<DiaryEntry> actualResult = diaryEntryService.get(INVALID_ID);
        assertThat(actualResult.isPresent()).isFalse();
    }

    @Test
    void testSaveDiaryEntry() {
        when(mockRepository.save(DIARY_ENTRY)).thenReturn(DIARY_ENTRY);

        DiaryEntry actualResult = diaryEntryService.save(DIARY_ENTRY);
        assertThat(actualResult).isEqualTo(DIARY_ENTRY);
    }

    @Test
    void testDeleteDiaryEntry() {
        doNothing().when(mockRepository).delete(DIARY_ENTRY);

       diaryEntryService.delete(VALID_ID);
        verify(mockRepository, times(1)).deleteById(VALID_ID);
    }
}