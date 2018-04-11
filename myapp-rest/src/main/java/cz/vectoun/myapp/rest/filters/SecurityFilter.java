package cz.vectoun.myapp.rest.filters;

import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.facade.UserFacade;
import cz.vectoun.myapp.rest.ApiUris;
import cz.vectoun.myapp.rest.security.SecurityUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
@WebFilter(urlPatterns = {
		ApiUris.ROOT_URI_USERS + "/*",
		ApiUris.ROOT_URI_NOTEGROUPS + "/*",
		ApiUris.ROOT_URI_NOTES + "/*"
})
public class SecurityFilter implements Filter {

	private static final String LOGIN_ERROR_MESSAGE = "{\"errors\":[\"User is not logged in.\"]}";

	@Override
	public void init(FilterConfig filterConfig) {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;


		if (request.getMethod().equals("OPTIONS")) {
			filterChain.doFilter(request, response);
			return;
		}

		Cookie[] cookies = request.getCookies();

		if (cookies == null) {
			sendUnAuthError(response);
			return;
		}

		String token = null;

		for (Cookie cookie : cookies) {
			if (SecurityUtils.AUTH_COOKIE.equals(cookie.getName())) {
				token = SecurityUtils.decrypt(SecurityUtils.KEY, SecurityUtils.INIT_VECTOR, cookie.getValue());
				break;
			}
		}

		if (token == null) {
			sendUnAuthError(response);
			return;
		}

		String[] data = token.split(";", 2);
		if (data.length != 2) {
			sendUnAuthError(response);
			return;
		}

		long id;
		String email;

		try {
			id = Long.parseLong(data[0]);
		} catch (NumberFormatException e) {
			sendUnAuthError(response);
			return;
		}
		email = data[1];

		UserFacade userFacade = WebApplicationContextUtils
				.getWebApplicationContext(servletRequest.getServletContext())
				.getBean(UserFacade.class);

		UserDTO user = userFacade.findUserById(id);
		if (user == null || !user.getEmail().equals(email)) {
			sendUnAuthError(response);
			return;
		}

		request.setAttribute(SecurityUtils.AUTHENTICATED_USER, user);

		filterChain.doFilter(request, response);
	}

	private void sendUnAuthError(HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter out = response.getWriter();
		out.print(LOGIN_ERROR_MESSAGE);
	}

	@Override
	public void destroy() {

	}
}
