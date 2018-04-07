package cz.vectoun.myapp.service.facade;

import cz.vectoun.myapp.api.facade.NoteFacade;
import cz.vectoun.myapp.facade.NoteFacadeImpl;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.service.BeanMappingService;
import cz.vectoun.myapp.service.NoteGroupService;
import cz.vectoun.myapp.service.NoteService;
import cz.vectoun.myapp.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class NoteFacadeUnitTest extends AbstractTestNGSpringContextTests {

    private NoteService noteService = mock(NoteService.class);
    private NoteGroupService noteGroupService = mock(NoteGroupService.class);

    private Note mockedNote = mock(Note.class);
    private NoteGroup mockedNoteGroup = mock(NoteGroup.class);

    //TODO: change this to mocked object
    @Inject
    private BeanMappingService beanMappingService;

    @InjectMocks
    private NoteFacade noteFacade;

    @BeforeMethod
    public void setFacade() {
        noteFacade = new NoteFacadeImpl(noteService, noteGroupService, beanMappingService);
    }

    @Test
    public void createNoteTest() {
        when(noteGroupService.findById(any())).thenReturn(mockedNoteGroup);

        noteFacade.createNote(1L);

        verify(noteService, times(1)).createNote(any(), eq(mockedNoteGroup));
    }

    @Test
    public void updateNoteTest() {
        when(noteService.findById(1L)).thenReturn(mockedNote);

        noteFacade.updateNote(1L, "Text");

        verify(mockedNote, times(1)).setText("Text");
    }

    @Test
    public void deleteNoteTest() {
        when(noteService.findById(1L)).thenReturn(mockedNote);

        noteFacade.deleteNote(1L);

        verify(noteService, times(1)).delete(mockedNote);
    }

    //TODO: implement missing tests
}
