package cz.vectoun.myapp.facade;

import cz.vectoun.myapp.api.dto.NoteDTO;
import cz.vectoun.myapp.api.facade.NoteFacade;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.service.BeanMappingService;
import cz.vectoun.myapp.service.NoteGroupService;
import cz.vectoun.myapp.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@Service
@Transactional
public class NoteFacadeImpl implements NoteFacade {

    private NoteService noteService;
    private NoteGroupService noteGroupService;
    private final BeanMappingService beanMappingService;

    @Inject
    public NoteFacadeImpl(NoteService noteService, NoteGroupService noteGroupService, BeanMappingService beanMappingService) {
        this.noteService = noteService;
        this.noteGroupService = noteGroupService;
        this.beanMappingService = beanMappingService;
    }

    @Override
    public NoteDTO createNote(Long noteGroupId) {
        if (noteGroupId == null) {
            throw new IllegalArgumentException("NoteGroupId can not be null");
        }
        NoteGroup foundGroup = noteGroupService.findById(noteGroupId);
        if (foundGroup == null) {
            throw new IllegalArgumentException("For given id does not exist any note group: " + noteGroupId);
        }

        Note note = new Note();
        note.setName("Nice Hack here");
        noteService.createNote(note, foundGroup);
        note.setName(note.getName() + note.getId());

        return beanMappingService.mapTo(note, NoteDTO.class);
    }

    @Override
    public NoteDTO updateNote(Long id, String text) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text can not be null");
        }

        Note foundNote = noteService.findById(id);
        if (foundNote == null) {
            throw new IllegalArgumentException("For given id does not exist any note: " + id);
        }

        foundNote.setText(text);

        return beanMappingService.mapTo(foundNote, NoteDTO.class);
    }

    @Override
    public NoteDTO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }

        return beanMappingService.mapTo(noteService.findById(id), NoteDTO.class);
    }

    @Override
    public void deleteNote(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }

        Note foundNote = noteService.findById(id);
        if (foundNote == null) {
            throw new IllegalArgumentException("For given id does not exist any note: " + id);
        }

        noteService.delete(foundNote);
    }
}
