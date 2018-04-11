package cz.vectoun.myapp.api.dto;

import java.util.Objects;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public class UpdateNoteGroupDTO {

    private String name;

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
        UpdateNoteGroupDTO that = (UpdateNoteGroupDTO) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UpdateNoteGroupDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
