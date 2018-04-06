package cz.vectoun.myapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
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
    private String text;

    public Note() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(getNoteGroup(), note.getNoteGroup()) &&
                Objects.equals(getText(), note.getText());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNoteGroup(), getText());
    }
}
