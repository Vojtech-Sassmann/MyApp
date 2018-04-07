package cz.vectoun.myapp.facade;

import cz.vectoun.myapp.api.dto.CreateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;
import cz.vectoun.myapp.api.facade.NoteGroupFacade;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.service.BeanMappingService;
import cz.vectoun.myapp.service.NoteGroupService;
import cz.vectoun.myapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@Service
@Transactional
public class NoteGroupFacadeImpl implements NoteGroupFacade {

    private BeanMappingService beanMappingService;
    private NoteGroupService noteGroupService;
    private UserService userService;

    @Inject
    public NoteGroupFacadeImpl(BeanMappingService beanMappingService,
                               NoteGroupService noteGroupService,
                               UserService userService) {
        this.beanMappingService = beanMappingService;
        this.noteGroupService = noteGroupService;
        this.userService = userService;
    }

    @Override
    public NoteGroupDTO createNoteGroup(CreateNoteGroupDTO group) {
        if (group == null) {
            throw new IllegalArgumentException("Group can not be null.");
        }
        if (group.getUserId() == null) {
            throw new IllegalArgumentException("UserId can not be null.");
        }
        String name = group.getName();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can not be empty or null: " + group);
        }

        User foundUser = userService.findById(group.getUserId());
        if (foundUser == null) {
            throw new IllegalArgumentException("For given id was no user found: " + group.getUserId());
        }

        NoteGroup noteGroup = new NoteGroup();
        noteGroup.setName(name);

        noteGroupService.createNoteGroup(noteGroup, foundUser);

        return beanMappingService.mapTo(noteGroupService.findById(noteGroup.getId()), NoteGroupDTO.class);
    }

    @Override
    public NoteGroupDTO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null.");
        }

        return beanMappingService.mapTo(noteGroupService.findById(id), NoteGroupDTO.class);
    }

    @Override
    public void deleteNoteGroup(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null.");
        }

        NoteGroup foundNoteGroup = noteGroupService.findById(id);

        if (foundNoteGroup == null) {
            throw new IllegalArgumentException("For given id was no note group found: " + id);
        }

        noteGroupService.delete(foundNoteGroup);
    }

    @Override
    public NoteGroupDTO updateNoteGroup(NoteGroupDTO newVersion) {
        if (newVersion == null) {
            throw new IllegalArgumentException("New version can not be null.");
        }
        if (newVersion.getId() == null) {
            throw new IllegalArgumentException("New version needs to have an id set.");
        }

        NoteGroup foundGroup = noteGroupService.findById(newVersion.getId());

        if (foundGroup == null) {
            throw new IllegalArgumentException("For given id was no group found: " + newVersion);
        }

        foundGroup.setName(newVersion.getName());

        return beanMappingService.mapTo(foundGroup, NoteGroupDTO.class);
    }

    @Override
    public List<NoteGroupDTO> getAllForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId can not be null.");
        }

        User foundUser = userService.findById(userId);

        if (foundUser == null) {
            throw new IllegalArgumentException("For given id was no user found: " + userId);
        }

        List<NoteGroup> noteGroups = noteGroupService.findAllForUser(foundUser);
        return beanMappingService.mapTo(noteGroups, NoteGroupDTO.class);
    }
}
