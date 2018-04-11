package cz.vectoun.myapp.api.dto;

import java.util.Objects;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public class RegisterUserDTO {

    private String unencryptedPassword;
    private String email;
    private String firstName;
    private String surname;

    public String getUnencryptedPassword() {
        return unencryptedPassword;
    }

    public void setUnencryptedPassword(String unencryptedPassword) {
        this.unencryptedPassword = unencryptedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterUserDTO that = (RegisterUserDTO) o;
        return Objects.equals(unencryptedPassword, that.unencryptedPassword) &&
                Objects.equals(email, that.email) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(surname, that.surname);
    }

    @Override
    public int hashCode() {

        return Objects.hash(unencryptedPassword, email, firstName, surname);
    }

    @Override
    public String toString() {
        return "RegisterUserDTO{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
