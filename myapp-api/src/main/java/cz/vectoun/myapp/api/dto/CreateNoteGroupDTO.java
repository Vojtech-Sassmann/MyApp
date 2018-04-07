package cz.vectoun.myapp.api.dto;

import java.util.Objects;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public class CreateNoteGroupDTO {

    private String name;

    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        if (o == null || getClass() != o.getClass()) return false;
        CreateNoteGroupDTO that = (CreateNoteGroupDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, userId);
    }

    @Override
    public String toString() {
        return "CreateNoteGroupDTO{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
