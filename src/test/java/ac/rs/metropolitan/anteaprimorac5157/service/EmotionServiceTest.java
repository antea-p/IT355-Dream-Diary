package ac.rs.metropolitan.anteaprimorac5157.service;

import ac.rs.metropolitan.anteaprimorac5157.entity.Emotion;
import ac.rs.metropolitan.anteaprimorac5157.repository.EmotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmotionServiceTest {

    private EmotionRepository mockRepository;
    private EmotionService emotionService;

    @BeforeEach
    void setUp() {
        this.mockRepository = mock(EmotionRepository.class);
        this.emotionService = new EmotionService(mockRepository);
    }

    @Test
    void testFindAllEmotions_ReturnsListOfEmotions() {
        List<Emotion> expectedResult = List.of(new Emotion(1, "Nostalgia"),
                                               new Emotion(2, "Wonder"));
        when(mockRepository.findAll()).thenReturn(expectedResult);

        List<Emotion> actualResult = emotionService.findAllEmotions();
        assertThat(actualResult).isEqualTo(expectedResult);
    }


}
