package cz.vectoun.myapp.persistance.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    private NoteGroup noteGroup;

    @NotNull
    @NotEmpty
    private String name;

    private String text;

    private LocalDate created;

    private LocalDate updated;

    public Note() {

    }

    @PrePersist
    protected void onCreate() {
        created = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDate.now();
    }

    public LocalDate getCreated() {
        return created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoteGroup getNoteGroup() {
        return noteGroup;
    }

    public void setNoteGroupBadly(NoteGroup noteGroup) {
        this.noteGroup = noteGroup;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(getName(), note.getName()) &&
                Objects.equals(getText(), note.getText()) &&
                Objects.equals(getCreated(), note.getCreated()) &&
                Objects.equals(getUpdated(), note.getUpdated());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getText(), getCreated(), getUpdated());
    }
}
