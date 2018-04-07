package cz.vectoun.myapp.rest.security;

import cz.vectoun.myapp.api.dto.NoteDTO;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.enums.UserRole;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface RoleResolver {

	/**
	 * Returns true if given request contains user with given role
	 *
	 * @param request request
	 * @param role role
	 * @return true if given request contains user with given role, false otherwise
	 */
	boolean hasRole(HttpServletRequest request, UserRole role);

	/**
	 * Returns true if user from request is given user
	 *
	 * @param request request
	 * @param user user
	 * @return Returns true if user from request has given id, false otherwise
	 */
	boolean isSelf(HttpServletRequest request, UserDTO user);

	/**
	 * Returns true if user from request is given user
	 *
	 * @param request request
	 * @param id id
	 * @return Returns true if user from request has given id, false otherwise
	 */
	boolean isSelfById(HttpServletRequest request, Long id);

	/**
	 * Returns user from current session
	 *
	 * @param request request
	 * @return user from session
	 */
	UserDTO getUser(HttpServletRequest request);

	/**
	 * Returns true if user from request can acces given note
	 *
	 * @param request request
	 * @param note note
	 * @return true if can access, false otherwise
	 */
    boolean canAccess(HttpServletRequest request, NoteDTO note);
}
