package cz.vectoun.myapp.persistance.dao;

import cz.vectoun.myapp.persistance.PersistenceApplicationContext;
import cz.vectoun.myapp.persistance.entity.Note;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@ContextConfiguration(classes = PersistenceApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class NoteDaoTest extends AbstractTestNGSpringContextTests {

    @Inject
    private NoteDao noteDao;

    @Test
    public void testCreateWithNull() {
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> noteDao.create(null));
    }

    @Test
    public void testCreate() {
        Note note = createTestNote();
        Note foundNote = noteDao.findById(note.getId());
        assertThat(foundNote).isEqualToComparingFieldByField(note);
    }

    @Test
    public void testDeleteWithNull() {
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> noteDao.delete(null));
    }

    @Test
    public void testDelete() {
        Note note = createTestNote();

        noteDao.delete(note);

        Note foundNote = noteDao.findById(note.getId());
        assertThat(foundNote).isEqualTo(null);
    }

    private Note createTestNote() {
        Note note = new Note();
        note.setText("asdfsdf");
        noteDao.create(note);
        return note;
    }
}
