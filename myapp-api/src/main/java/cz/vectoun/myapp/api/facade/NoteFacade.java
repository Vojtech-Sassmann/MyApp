package cz.vectoun.myapp.api.facade;

import cz.vectoun.myapp.api.dto.NoteDTO;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface NoteFacade {

    /**
     * Creates new note in given note group
     *
     * @return newly created note
     */
    NoteDTO createNote(Long noteGroupId);

    /**
     * Updates the text of given note
     *
     * @param id id of note to be updated
     * @param text text that will be set to given note
     * @return updated note
     */
    NoteDTO updateNote(Long id, String text);

    /**
     * Deletes the given note
     *
     * @param id if of note to be deleted
     */
    void deleteNote(Long id);
}
