package cz.vectoun.myapp.rest.security;

import cz.vectoun.myapp.api.dto.NoteDTO;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.enums.UserRole;
import cz.vectoun.myapp.persistance.entity.Note;
import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.service.NoteService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
@Component
public class RoleResolverImpl implements RoleResolver {

	private final NoteService noteService;

	@Inject
	public RoleResolverImpl(NoteService noteService) {
		this.noteService = noteService;
	}

	@Override
	public boolean hasRole(HttpServletRequest request, UserRole role) {

		UserDTO user = getUser(request);

		if (user == null || user.getRole() == null) {
			return false;
		}

		return user.getRole().equals(role);
	}

	@Override
	public boolean isSelf(HttpServletRequest request, UserDTO user) {
		UserDTO userFromRequest = getUser(request);

		return user.equals(userFromRequest);
	}

	@Override
	public boolean isSelfById(HttpServletRequest request, Long id) {
		UserDTO userFromRequest = getUser(request);

		return userFromRequest.getId().equals(id);
	}

	@Override
	public UserDTO getUser(HttpServletRequest request) {
		return (UserDTO) request.getAttribute(SecurityUtils.AUTHENTICATED_USER);
	}

	@Override
	public boolean canAccess(HttpServletRequest request, NoteDTO note) {
		UserDTO userFromRequest = getUser(request);
		Note foundNote = noteService.findById(note.getId());
		NoteGroup noteGroup = foundNote.getNoteGroup();

		return noteGroup.getOwner().getId().equals(userFromRequest.getId());
	}
}
