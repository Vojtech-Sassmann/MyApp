package cz.vectoun.myapp.persistance.entity;


import cz.vectoun.myapp.persistance.enums.UserRole;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String surname;

    @Email
    @NotNull
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated
    @NotNull
    @Column(nullable = false)
    private UserRole role;

    @NotNull
    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<NoteGroup> noteGroups = new HashSet<>();

    public User() {
    }

    public Set<NoteGroup> getNoteGroups() {
        return Collections.unmodifiableSet(noteGroups);
    }

    public void addNoteGroup(NoteGroup group) {
        this.noteGroups.add(group);
        group.setOwnerBadly(this);
    }

    public void removeNoteGroup(NoteGroup group) {
        group.setOwnerBadly(null);
        this.noteGroups.remove(group);
    }

    public User(String firstName, String surname, String email, UserRole role) {
        checkString(firstName, "Name");
        checkString(surname, "Surname");
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        checkString(name, "Name");
        this.firstName = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        checkString(surname, "Surname");
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;

        return getFirstName().equals(user.getFirstName()) &&
                getSurname().equals(user.getSurname()) &&
                getEmail().equals(user.getEmail()) &&
                getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getSurname(), getEmail(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    private void checkString(String value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name +" cannot be null.");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty.");
        }
    }
}
