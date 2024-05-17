package ac.rs.metropolitan.anteaprimorac5157.repository;

import ac.rs.metropolitan.anteaprimorac5157.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Integer> {
//    TODO: nove metode po potrebi (za sortiranje, filtriranje i sl.)

    List<DiaryEntry> findAllByOrderByTitleAsc();

}
