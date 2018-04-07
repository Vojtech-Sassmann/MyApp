package cz.vectoun.myapp.rest.controllers;

import cz.vectoun.myapp.api.dto.CreateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;
import cz.vectoun.myapp.api.enums.UserRole;
import cz.vectoun.myapp.api.facade.NoteGroupFacade;
import cz.vectoun.myapp.rest.ApiUris;
import cz.vectoun.myapp.rest.exceptions.PrivilegeException;
import cz.vectoun.myapp.rest.security.RoleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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

    @Inject
    public NoteGroupController(NoteGroupFacade noteGroupFacade, RoleResolver roleResolver) {
        this.noteGroupFacade = noteGroupFacade;
        this.roleResolver = roleResolver;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<NoteGroupDTO> getAllForUser(@RequestParam("user") Long userId, HttpServletRequest request) {

        log.debug("rest getAll noteGroups for {}", userId);

        if (!(roleResolver.isSelfById(request, userId) ||
                roleResolver.hasRole(request, UserRole.ADMIN))) {
            throw new PrivilegeException("Not permitted.");
        }

        return noteGroupFacade.getAllForUser(userId);
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
}
