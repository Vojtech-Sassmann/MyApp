package cz.vectoun.myapp.rest.controllers;

import cz.vectoun.myapp.api.dto.NoteDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;
import cz.vectoun.myapp.api.dto.UpdateNoteDTO;
import cz.vectoun.myapp.api.enums.UserRole;
import cz.vectoun.myapp.api.facade.NoteFacade;
import cz.vectoun.myapp.api.facade.NoteGroupFacade;
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

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@RestController
@RequestMapping(ApiUris.ROOT_URI_NOTES)
public class NoteController {

    private static final Logger log = LoggerFactory.getLogger(NoteController.class);

    private final NoteFacade noteFacade;
    private final NoteGroupFacade noteGroupFacade;
    private final RoleResolver roleResolver;

    @Inject
    public NoteController(NoteFacade noteFacade, NoteGroupFacade noteGroupFacade, RoleResolver roleResolver) {
        this.noteFacade = noteFacade;
        this.noteGroupFacade = noteGroupFacade;
        this.roleResolver = roleResolver;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public NoteDTO createNote(@RequestParam Long noteGroupId, HttpServletRequest request) {

        log.debug("rest createNote({})", noteGroupId);

        NoteGroupDTO noteGroup = noteGroupFacade.findById(noteGroupId);

        if (noteGroup == null) {
            throw new ResourceNotFoundException("Given note group does not exist.");
        }

        if (!(roleResolver.isSelf(request, noteGroup.getOwner()) ||
                roleResolver.hasRole(request, UserRole.ADMIN))) {
            throw new PrivilegeException("Not permitted.");
        }

        return noteFacade.createNote(noteGroupId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public NoteDTO updateNote(@PathVariable("id") long id, @RequestBody UpdateNoteDTO update, HttpServletRequest request) {

        log.debug("rest updateNote({} , {})", id, update);

        NoteDTO note = noteFacade.findById(id);

        if (note == null) {
            throw new ResourceNotFoundException("Given note does not exist");
        }

        if (!(roleResolver.canAccess(request, note)) ||
                roleResolver.hasRole(request, UserRole.ADMIN)) {
            throw new PrivilegeException("Not permitted.");
        }

        return noteFacade.updateNote(id, update.getText());
    }
}
