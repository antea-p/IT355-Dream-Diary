package ac.rs.metropolitan.anteaprimorac5157.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tags", schema = "dream_diary")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "entry_id", nullable = false)
    private DiaryEntry entry;

    public Tag() {
    }

    public Tag(String name, DiaryEntry entry) {
        this.name = name;
        this.entry = entry;
    }

    public Tag(Integer id, String name, DiaryEntry entry) {
        this.id = id;
        this.name = name;
        this.entry = entry;
    }

    public Integer getId() {
        return id;
    }

    public Tag setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    public DiaryEntry getEntry() {
        return entry;
    }

    public Tag setEntry(DiaryEntry entry) {
        this.entry = entry;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) && Objects.equals(name, tag.name) && Objects.equals(entry, tag.entry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entry);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
