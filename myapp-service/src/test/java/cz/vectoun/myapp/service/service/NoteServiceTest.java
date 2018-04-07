package cz.vectoun.myapp.service.service;

import cz.vectoun.myapp.persistance.dao.NoteDao;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.service.NoteService;
import cz.vectoun.myapp.service.NoteServiceImpl;
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
public class NoteServiceTest {

    private NoteDao noteDao = mock(NoteDao.class);
    private NoteGroup mockedNoteGroup;
    private Note mockedNote1;
    private Note mockedNote2;

    @InjectMocks
    private NoteService noteService;

    @BeforeMethod
    public void setService() {
        mockedNote1 = mock(Note.class);
        mockedNote2 = mock(Note.class);

        mockedNoteGroup = mock(NoteGroup.class);

        noteService = new NoteServiceImpl(noteDao);
    }

    @Test
    public void createTest() {
        noteService.createNote(mockedNote1, mockedNoteGroup);

        verify(noteDao, times(1)).create(mockedNote1);
        verify(mockedNoteGroup, times(1)).addNote(mockedNote1);
    }

    @Test
    public void deleteTest() {
        noteService.delete(mockedNote1);

        verify(noteDao, times(1)).delete(mockedNote1);
    }

    @Test
    public void getAllTest() {
        when(noteDao.getAll()).thenReturn(Arrays.asList(mockedNote1, mockedNote2));

        List<Note> foundNotes = noteService.getAll();

        assertThat(foundNotes).containsOnly(mockedNote1, mockedNote2);
    }
}
