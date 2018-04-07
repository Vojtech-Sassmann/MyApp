package cz.vectoun.myapp.service;

import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface NoteService extends Service<Note> {

    /**
     * Creates new note for given note
     *
     * @param note note that will be created
     * @param noteGroup owner entity of the newly created note
     */
    void createNote(Note note, NoteGroup noteGroup);
}
