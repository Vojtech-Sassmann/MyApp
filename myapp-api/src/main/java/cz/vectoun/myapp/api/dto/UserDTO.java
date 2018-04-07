package cz.vectoun.myapp.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.vectoun.myapp.api.enums.UserRole;

import java.util.Objects;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public class UserDTO {
    private Long id;
    private String firstName;
    private String surname;
    private String email;
    private String passwordHash;
    private UserRole role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonProperty
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
                Objects.equals(firstName, userDTO.firstName) &&
                Objects.equals(surname, userDTO.surname) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(passwordHash, userDTO.passwordHash) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, surname, email, passwordHash, role);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                '}';
    }
}
