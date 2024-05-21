package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryUser;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryEntryRepository;
import ac.rs.metropolitan.anteaprimorac5157.repository.DiaryUserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final DiaryUserRepository diaryUserRepository;

    public DiaryEntryService(DiaryEntryRepository diaryEntryRepository, DiaryUserRepository diaryUserRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.diaryUserRepository = diaryUserRepository;
    }

    public List<DiaryEntry> list(@Nullable Sort.Direction direction, @Nullable DiaryEntrySortingCriteria sortBy,
                                 @Nullable String title) {
        Sort sort = Sort.by(direction == null ? Sort.Direction.ASC : direction,
                sortBy == null ? "id" : sortBy.toString());
        return (title == null)? diaryEntryRepository.findAll(sort) :
                diaryEntryRepository.findByTitleContainingIgnoreCase(title, sort);
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

    // TODO: improve performance
    public Map<String, Integer> getDiaryEntryCountByUser() {
        List<DiaryUser> users = diaryUserRepository.findAll();
        Map<String, Integer> diaryCountByUser = new HashMap<>();

        for (DiaryUser user : users) {
            int count = diaryEntryRepository.countByUserId(user.getId());
            diaryCountByUser.put(user.getUsername(), count);
        }

        return diaryCountByUser;
    }
}