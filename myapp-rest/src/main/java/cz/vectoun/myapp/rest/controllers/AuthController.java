package cz.vectoun.myapp.rest.controllers;

import cz.vectoun.myapp.api.dto.AuthDTO;
import cz.vectoun.myapp.api.dto.UserAuthenticateDTO;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.facade.UserFacade;
import cz.vectoun.myapp.rest.ApiUris;
import cz.vectoun.myapp.rest.exceptions.NotAuthorizedException;
import cz.vectoun.myapp.rest.exceptions.ResourceNotFoundException;
import cz.vectoun.myapp.rest.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
@RestController
@RequestMapping(ApiUris.ROOT_AUTH)
public class AuthController {

	private final static Logger log = LoggerFactory.getLogger(AuthController.class);

	private final UserFacade userFacade;

	@Inject
	public AuthController(UserFacade userFacade) {
		this.userFacade = userFacade;
	}

	/**
	 * Authenticate User by identifier and password curl -i -X POST
	 * 'http://localhost:8080/rest/auth'
	 *
	 * @return true if authentication succeeded
	 */
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public final boolean authenticate(@RequestBody AuthDTO authDTO, HttpServletResponse response,
									  HttpServletRequest request) throws Exception {

		log.debug("rest authenticate({})", authDTO);

		UserDTO userWithEmail = userFacade.findUserByEmail(authDTO.getEmail());
		if (userWithEmail == null) {
			throw new NotAuthorizedException("Invalid login combination.");
		}

		UserAuthenticateDTO userAuthenticateDTO = new UserAuthenticateDTO();
		userAuthenticateDTO.setPassword(authDTO.getPassword());
		userAuthenticateDTO.setUserId(userWithEmail.getId());

		try {
			if(userFacade.authenticate(userAuthenticateDTO)) {
				UserDTO user = userFacade.findUserById(userAuthenticateDTO.getUserId());

				Cookie authCookie = SecurityUtils.generateAuthCookie(user);
				Cookie isAdminCookie = SecurityUtils.generateIsAdminCookie(user);

				response.addCookie(authCookie);
				response.addCookie(isAdminCookie);
				return true;
			} else {
				throw new NotAuthorizedException("Invalid login combination.");
			}
		} catch(Exception e) {
			throw new ResourceNotFoundException("User not found");
		}
	}

	/**
	 * Logout user curl -i -X DELETE
	 * 'http://localhost:8080/pa165/rest/auth?email=alojz@hlinka.com&password=1234'
	 *
	 * @return true if logout succeeded
	 */
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public final boolean logout(HttpServletRequest request, HttpServletResponse response) {

		log.debug("rest logout()");

		Cookie[] cookies = request.getCookies();

		Cookie authCookie = null;
		Cookie isAdminCookie = null;

		for (Cookie cookie : cookies) {
			if (SecurityUtils.AUTH_COOKIE.equals(cookie.getName())) {
				authCookie = cookie;
			} else if (SecurityUtils.IS_ADMIN_COOKIE.equals(cookie.getName())){
				isAdminCookie = cookie;
			}
		}

		if (authCookie == null || isAdminCookie == null) {
			return false;
		}
		authCookie.setPath("/");
		authCookie.setMaxAge(0);
		isAdminCookie.setMaxAge(0);
		isAdminCookie.setPath("/");
		response.addCookie(authCookie);
		response.addCookie(isAdminCookie);
		return true;
	}
}
