package cz.vectoun.myapp.persistance.dao.integration;

import cz.vectoun.myapp.persistance.PersistenceApplicationContext;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.dao.NoteDao;
import cz.vectoun.myapp.persistance.dao.NoteGroupDao;
import cz.vectoun.myapp.persistance.dao.UserDao;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.persistance.enums.UserRole;
import org.assertj.core.api.Assertions;
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
    public void addNodeGroupToUser() {
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

    @Test
    public void removeNoteFromNoteGroup() {
        // create note group and assign note to it
        User user = createRegularUser();
        NoteGroup noteGroup = createNoteGroup(user);
        Note note = createNote();
        noteGroup.addNote(note);

        NoteGroup foundGroup = noteGroupDao.findById(noteGroup.getId());
        foundGroup.removeNote(note);

        NoteGroup foundGroup2 = noteGroupDao.findById(noteGroup.getId());
        Assertions.assertThat(foundGroup2.getNotes()).doesNotContain(note);
    }

    @Test
    public void removeNoteGroupFromUser() {
        User user = createRegularUser();
        NoteGroup noteGroup = createNoteGroup(user);
        Set<NoteGroup> userNoteGroups = userDao.findById(user.getId()).getNoteGroups();

        user.removeNoteGroup(new ArrayList<>(userNoteGroups).get(0));

        User foundUser = userDao.findById(user.getId());

        assertThat(foundUser.getNoteGroups()).doesNotContain(noteGroup);
    }


    // ----------- private methods ------------

    private Note createNote() {
        Note note = new Note();
        note.setText(String.valueOf(index++));
        noteDao.create(note);
        return note;
    }

    private User createRegularUser() {
        User user = new User();
        user.setFirstName(String.valueOf(index++));
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
