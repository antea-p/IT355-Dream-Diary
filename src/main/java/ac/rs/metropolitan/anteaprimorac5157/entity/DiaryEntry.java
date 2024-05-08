package ac.rs.metropolitan.anteaprimorac5157.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "diary_entries", schema = "dream_diary")
public class DiaryEntry {

    // TODO: Long ID za ovaj i za druge entitete, AKO bude trebalo.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DiaryUser user;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryEntryEmotion> diaryEntryEmotions = new ArrayList<>();

    public DiaryEntry() {
    }

    public DiaryEntry(Integer id, String title, String content, LocalDate createdDate, DiaryUser user, List<Tag> tags, List<DiaryEntryEmotion> diaryEntryEmotions) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.user = user;
        this.tags = tags;
        this.diaryEntryEmotions = diaryEntryEmotions;
    }

    public Integer getId() {
        return id;
    }

    public DiaryEntry setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DiaryEntry setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DiaryEntry setContent(String content) {
        this.content = content;
        return this;
    }

    public DiaryUser getUser() {
        return user;
    }

    public DiaryEntry setUser(DiaryUser user) {
        this.user = user;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public DiaryEntry setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public List<DiaryEntryEmotion> getDiaryEntryEmotions() {
        return diaryEntryEmotions;
    }

    public DiaryEntry setDiaryEntryEmotions(List<DiaryEntryEmotion> diaryEntryEmotions) {
        this.diaryEntryEmotions = diaryEntryEmotions;
        return this;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public DiaryEntry setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiaryEntry that = (DiaryEntry) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(createdDate, that.createdDate) && Objects.equals(user, that.user) && Objects.equals(tags, that.tags) && Objects.equals(diaryEntryEmotions, that.diaryEntryEmotions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createdDate, user, tags, diaryEntryEmotions);
    }

    @Override
    public String toString() {
        return "DiaryEntry{" +
                "id=" + id +
                ", title=" + title +
                ", content=" + content +
                ", createdDate=" + createdDate +
                ", user=" + user +
                ", tags=" + tags +
                ", diaryEntryEmotions=" + diaryEntryEmotions +
                '}';
    }

}
