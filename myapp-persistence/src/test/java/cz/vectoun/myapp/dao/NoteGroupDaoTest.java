package cz.vectoun.myapp.dao;

import cz.vectoun.myapp.PersistenceApplicationContext;
import cz.vectoun.myapp.entity.NoteGroup;
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
public class NoteGroupDaoTest extends AbstractTestNGSpringContextTests {

    @Inject
    private NoteGroupDao noteGroupDao;

    @Test
    public void testCreateWithNull() {
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> noteGroupDao.create(null));
    }

    @Test
    public void testCreate() {
        NoteGroup noteGroup = createTestNoteGroup();
        NoteGroup foundNoteGroup = noteGroupDao.findById(noteGroup.getId());
        assertThat(foundNoteGroup).isEqualToComparingFieldByField(noteGroup);
    }

    private NoteGroup createTestNoteGroup() {
        NoteGroup group = new NoteGroup();
        group.setName("TODO");
        noteGroupDao.create(group);
        return group;
    }

    @Test
    public void testDeleteWithNull() {
        assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> noteGroupDao.delete(null));
    }

    @Test
    public void testDelete() {
        NoteGroup noteGroup = createTestNoteGroup();

        noteGroupDao.delete(noteGroup);

        NoteGroup foundNoteGroup = noteGroupDao.findById(noteGroup.getId());
        assertThat(foundNoteGroup).isEqualTo(null);
    }
}
