package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.repository.EmotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionService {

    private final EmotionRepository emotionRepository;

    public EmotionService(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    public List<Emotion> findAllEmotions() {
        return emotionRepository.findAll();
    }


}
