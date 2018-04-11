package cz.vectoun.myapp.persistance.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class NoteGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne
    private User owner;

    @OneToMany(
            mappedBy = "noteGroup",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private Set<Note> notes = new HashSet<>();

    public NoteGroup() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public Set<Note> getNotes() {
        return Collections.unmodifiableSet(notes);
    }

    public void addNote(Note note) {
        note.setNoteGroupBadly(this);
        this.notes.add(note);
    }

    public void removeNote(Note note) {
        note.setNoteGroupBadly(null);
        this.notes.remove(note);
    }

    public void setOwnerBadly(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteGroup)) return false;
        NoteGroup that = (NoteGroup) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getOwner(), that.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getOwner());
    }
}
