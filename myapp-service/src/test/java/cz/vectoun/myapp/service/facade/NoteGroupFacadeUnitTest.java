package cz.vectoun.myapp.service.facade;

import cz.vectoun.myapp.api.dto.CreateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;
import cz.vectoun.myapp.api.dto.UpdateNoteGroupDTO;
import cz.vectoun.myapp.api.facade.NoteGroupFacade;
import cz.vectoun.myapp.facade.NoteGroupFacadeImpl;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.service.BeanMappingService;
import cz.vectoun.myapp.service.NoteGroupService;
import cz.vectoun.myapp.service.UserService;
import cz.vectoun.myapp.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class NoteGroupFacadeUnitTest extends AbstractTestNGSpringContextTests {

    private NoteGroupService noteGroupService = mock(NoteGroupService.class);
    private UserService userService = mock(UserService.class);

    private User mockedUser = mock(User.class);
    private NoteGroup mockedNoteGroup = mock(NoteGroup.class);

    private NoteGroup noteGroup;

    @Inject
    private BeanMappingService beanMappingService;

    @InjectMocks
    private NoteGroupFacade noteGroupFacade;

    @BeforeMethod
    public void setUpFacade() {
        noteGroupFacade = new NoteGroupFacadeImpl(beanMappingService, noteGroupService, userService);
        noteGroup = new NoteGroup();
        noteGroup.setName("Aha");
        noteGroup.setId(5L);
    }

    @Test
    public void testCreateNoteGroup() {
        when(userService.findById(anyLong())).thenReturn(mockedUser);

        CreateNoteGroupDTO specification = new CreateNoteGroupDTO();
        specification.setUserId(1L);
        specification.setName("TODOs");

        noteGroupFacade.createNoteGroup(specification);

        verify(noteGroupService, times(1)).createNoteGroup(any(), eq(mockedUser));
    }

    @Test
    public void testUpdateNoteGroup() {
        when(noteGroupService.findById(1L)).thenReturn(mockedNoteGroup);

        UpdateNoteGroupDTO newVersion = new UpdateNoteGroupDTO();
        newVersion.setName("AHAH");

        noteGroupFacade.updateNoteGroup(1L, newVersion);

        verify(mockedNoteGroup, times(1)).setName(newVersion.getName());
    }

    @Test
    public void testDeleteNoteGroup() {
        when(noteGroupService.findById(1L)).thenReturn(mockedNoteGroup);

        noteGroupFacade.deleteNoteGroup(1L);

        verify(noteGroupService, times(1)).delete(mockedNoteGroup);
    }

    @Test
    public void getAllForUser() {
        when(noteGroupService.findAllForUser(mockedUser)).thenReturn(Collections.singletonList(noteGroup));
        when(userService.findById(1L)).thenReturn(mockedUser);

        List<NoteGroupDTO> foundGroups = noteGroupFacade.getAllForUser(1L);

        assertThat(foundGroups).hasSize(1);
        assertThat(foundGroups.get(0).getName()).isEqualTo(noteGroup.getName());
        assertThat(foundGroups.get(0).getId()).isEqualTo(noteGroup.getId());
    }
}
