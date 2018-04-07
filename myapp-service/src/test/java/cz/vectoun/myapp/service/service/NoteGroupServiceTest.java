package cz.vectoun.myapp.service.service;

import cz.vectoun.myapp.persistance.dao.NoteGroupDao;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.service.NoteGroupService;
import cz.vectoun.myapp.service.NoteGroupServiceImpl;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public class NoteGroupServiceTest {

    private NoteGroupDao noteGroupDao = mock(NoteGroupDao.class);
    private User mockedUser;
    private NoteGroup mockedNoteGroup1;
    private NoteGroup mockedNoteGroup2;

    @InjectMocks
    private NoteGroupService noteGroupService;

    @BeforeMethod
    public void setService() {
        noteGroupService = new NoteGroupServiceImpl(noteGroupDao);
    }

    @BeforeMethod
    public void createEntities() {
        mockedUser = mock(User.class);

        mockedNoteGroup1 = mock(NoteGroup.class);
        mockedNoteGroup2 = mock(NoteGroup.class);
    }

    @Test
    public void createNoteGroupTest() {
        noteGroupService.createNoteGroup(mockedNoteGroup1, mockedUser);

        verify(noteGroupDao, times(1)).create(mockedNoteGroup1);
        verify(mockedUser, times(1)).addNoteGroup(mockedNoteGroup1);
    }

    @Test
    public void deleteNoteGroupTest() {
        noteGroupService.delete(mockedNoteGroup1);

        verify(noteGroupDao, times(1)).delete(mockedNoteGroup1);
    }

    @Test
    public void getAllTest() {
        when(noteGroupDao.getAll()).thenReturn(Arrays.asList(mockedNoteGroup1, mockedNoteGroup2));

        List<NoteGroup> allNoteGroups = noteGroupService.getAll();
        assertThat(allNoteGroups).containsOnly(mockedNoteGroup1, mockedNoteGroup2);
    }

    @Test
    public void FindByIdTest() {
        when(noteGroupDao.findById(1L)).thenReturn(mockedNoteGroup1);

        NoteGroup foundGroup = noteGroupService.findById(1L);

        assertThat(foundGroup).isEqualTo(mockedNoteGroup1);
    }
}
