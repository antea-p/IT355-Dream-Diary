package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryEntryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;

    public DiaryEntryService(DiaryEntryRepository diaryEntryRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
    }

    public List<DiaryEntry> list(@Nullable Sort.Direction direction, @Nullable DiaryEntrySortingCriteria sortBy) {
        Sort sort = Sort.by(direction == null ? Sort.Direction.ASC : direction,
                sortBy == null ? "id" : sortBy.toString());
        return diaryEntryRepository.findAll(sort);
    }

    public Optional<DiaryEntry> get(Integer id) {
        return diaryEntryRepository.findById(id);
    }

    public DiaryEntry save(DiaryEntry diaryEntry) {
        return diaryEntryRepository.save(diaryEntry);
    }

    public void delete(Integer id) {
        diaryEntryRepository.deleteById(id);
    }
}