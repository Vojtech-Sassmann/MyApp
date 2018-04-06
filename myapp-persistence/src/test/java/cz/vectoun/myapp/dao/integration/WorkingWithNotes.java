package cz.vectoun.myapp.dao.integration;

import cz.vectoun.myapp.PersistenceApplicationContext;
import cz.vectoun.myapp.dao.NoteDao;
import cz.vectoun.myapp.dao.NoteGroupDao;
import cz.vectoun.myapp.dao.UserDao;
import cz.vectoun.myapp.entity.Note;
import cz.vectoun.myapp.entity.NoteGroup;
import cz.vectoun.myapp.entity.User;
import cz.vectoun.myapp.enums.UserRole;
import org.testng.annotations.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@ContextConfiguration(classes = PersistenceApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class WorkingWithNotes extends AbstractTestNGSpringContextTests {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private NoteGroupDao noteGroupDao;

    @Inject
    private UserDao userDao;

    @Inject
    private NoteDao noteDao;

    private int index = 0;

    @Test
    public void addUserToNodeGroup() {
        User user = createRegularUser();
        NoteGroup noteGroup = createNoteGroup(user);
        Set<NoteGroup> userNoteGroups = userDao.findById(user.getId()).getNoteGroups();

        assertThat(userNoteGroups).contains(noteGroup);
        User foundUser = new ArrayList<>(userNoteGroups).get(0).getOwner();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void addNotesToNoteGroup() {
        User user = createRegularUser();
        NoteGroup noteGroup = createNoteGroup(user);
        Note note = createNote();
        noteGroup.addNote(note);

        Set<Note> notes = noteGroupDao.findById(noteGroup.getId()).getNotes();

        assertThat(notes).contains(note);
        NoteGroup foundGroup = new ArrayList<>(notes).get(0).getNoteGroup();
        assertThat(foundGroup).isEqualTo(noteGroup);
    }

    private Note createNote() {
        Note note = new Note();
        note.setText(String.valueOf(index++));
        noteDao.create(note);
        return note;
    }

    private User createRegularUser() {
        User user = new User();
        user.setName(String.valueOf(index++));
        user.setSurname(String.valueOf(index++));
        user.setEmail(String.valueOf(index++) + "@aa.cz");
        user.setRole(UserRole.REGULAR);
        user.setPasswordHash(String.valueOf(index++));
        userDao.create(user);
        return user;
    }

    private NoteGroup createNoteGroup(User user) {
        NoteGroup group = new NoteGroup();
        group.setName(String.valueOf(index++));
        user.addNoteGroup(group);
        noteGroupDao.create(group);
        return group;
    }
}
