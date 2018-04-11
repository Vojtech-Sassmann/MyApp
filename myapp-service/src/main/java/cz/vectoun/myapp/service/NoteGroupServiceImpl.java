package cz.vectoun.myapp.service;

import cz.vectoun.myapp.persistance.dao.NoteGroupDao;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@Service
public class NoteGroupServiceImpl extends AbstractService<NoteGroup> implements NoteGroupService {

    private NoteGroupDao noteGroupDao;

    @Inject
    public NoteGroupServiceImpl(NoteGroupDao noteGroupDao) {
        super(noteGroupDao);

        this.noteGroupDao = noteGroupDao;
    }

    @Override
    public void createNoteGroup(NoteGroup noteGroup, User user) {
        user.addNoteGroup(noteGroup);

        noteGroupDao.create(noteGroup);
    }

    @Override
    public List<NoteGroup> findAllForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Given user can not be null.");
        }

        return noteGroupDao.findAllForUser(user);
    }
}
