package cz.vectoun.myapp.service;

import cz.vectoun.myapp.persistance.dao.NoteDao;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@Service
public class NoteServiceImpl extends AbstractService<Note> implements NoteService {

    private NoteDao noteDao;

    @Inject
    public NoteServiceImpl(NoteDao noteDao) {
        super(noteDao);

        this.noteDao = noteDao;
    }

    @Override
    public void createNote(Note note, NoteGroup noteGroup) {
        noteGroup.addNote(note);

        noteDao.create(note);
    }
}
