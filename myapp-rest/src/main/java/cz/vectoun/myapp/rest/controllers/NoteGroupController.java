package cz.vectoun.myapp.rest.controllers;

import cz.vectoun.myapp.api.dto.CreateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;
import cz.vectoun.myapp.api.dto.UpdateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.enums.UserRole;
import cz.vectoun.myapp.api.facade.NoteGroupFacade;
import cz.vectoun.myapp.api.facade.UserFacade;
import cz.vectoun.myapp.rest.ApiUris;
import cz.vectoun.myapp.rest.exceptions.PrivilegeException;
import cz.vectoun.myapp.rest.exceptions.ResourceNotFoundException;
import cz.vectoun.myapp.rest.security.RoleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_NOTEGROUPS)
public class NoteGroupController {

    private final static Logger log = LoggerFactory.getLogger(NoteGroupController.class);

    private final NoteGroupFacade noteGroupFacade;
    private final RoleResolver roleResolver;
    private final UserFacade userFacade;

    @Inject
    public NoteGroupController(NoteGroupFacade noteGroupFacade, RoleResolver roleResolver, UserFacade userFacade) {
        this.noteGroupFacade = noteGroupFacade;
        this.roleResolver = roleResolver;
        this.userFacade = userFacade;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<NoteGroupDTO> getAllForUser(HttpServletRequest request) {

        log.debug("rest getAll noteGroups");

//        UserDTO user = userFacade.findUserById(userId);

        // no user specified so return notes for logged user
//        if (user == null) {
            UserDTO user = roleResolver.getUser(request);
//        }

        if (!(roleResolver.isSelfById(request, user.getId()) ||
                roleResolver.hasRole(request, UserRole.ADMIN))) {
            throw new PrivilegeException("Not permitted.");
        }

        return noteGroupFacade.getAllForUser(user.getId());
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public NoteGroupDTO createNoteGroup(@RequestBody CreateNoteGroupDTO noteGroupDTO, HttpServletRequest request) {

        log.debug("rest createNoteGroup {}", noteGroupDTO);

        if (!(roleResolver.isSelfById(request, noteGroupDTO.getUserId()) ||
                roleResolver.hasRole(request, UserRole.ADMIN))) {
            throw new PrivilegeException("Not permitted.");
        }

        return noteGroupFacade.createNoteGroup(noteGroupDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteNoteGroup(@PathVariable("id") long id, HttpServletRequest request) {

        log.debug("rest deleteNoteGroup({})", id);

        NoteGroupDTO noteGroup = noteGroupFacade.findById(id);

        if (noteGroup == null) {
            throw new ResourceNotFoundException("Note group with given id does not exist.");
        }

        if (!(roleResolver.isSelfById(request, noteGroup.getId()) ||
                roleResolver.hasRole(request, UserRole.ADMIN))) {
            throw new PrivilegeException("Not permitted.");
        }

        noteGroupFacade.deleteNoteGroup(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateNoteGroup(@PathVariable("id") long id, @RequestBody UpdateNoteGroupDTO update,
                                HttpServletRequest request) {

        log.debug("rest updateNoteGroup({})", id);

        NoteGroupDTO noteGroup = noteGroupFacade.findById(id);

        if (noteGroup == null) {
            throw new ResourceNotFoundException("Note group with given id does not exist.");
        }

        if (!(roleResolver.isSelfById(request, noteGroup.getId()) ||
                roleResolver.hasRole(request, UserRole.ADMIN))) {
            throw new PrivilegeException("Not permitted.");
        }

        noteGroupFacade.updateNoteGroup(id, update);
    }
}
