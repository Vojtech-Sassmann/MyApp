package cz.vectoun.myapp.persistance.dao;

import cz.vectoun.myapp.persistance.PersistenceApplicationContext;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.persistance.enums.UserRole;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.util.List;

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

    @Inject
    private UserDao userDao;

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

    @Test
    public void testGetAllForUser() {
        User owner = new User();
        owner.setFirstName("Adolf");
        owner.setSurname("Hitler");
        owner.setRole(UserRole.ADMIN);
        owner.setEmail("Adolfinek@hitlerek.de");
        owner.setPasswordHash("sdfsdfasdfsdf323434h3j4h3j4hk3j4h");

        User other = new User();
        other.setFirstName("Ja");
        other.setSurname("Moje");
        other.setRole(UserRole.ADMIN);
        other.setEmail("ja@moje.de");
        other.setPasswordHash("sdfsdfsdf333h3j4h3j4hk3j4h");

        userDao.create(owner);
        userDao.create(other);

        NoteGroup ng1 = new NoteGroup();
        NoteGroup ng2 = new NoteGroup();
        ng1.setName("TODOs");
        ng2.setName("TODOs 2");

        owner.addNoteGroup(ng1);
        other.addNoteGroup(ng2);

        List<NoteGroup> foundGroups = noteGroupDao.findAllForUser(owner);

        assertThat(foundGroups).containsOnly(ng1);
    }
}
