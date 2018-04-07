package cz.vectoun.myapp.rest.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.facade.UserFacade;
import cz.vectoun.myapp.rest.security.RoleResolver;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class UsersControllerTest {

	private UserFacade userFacade = mock(UserFacade.class);
	private RoleResolver roleResolver = mock(RoleResolver.class);

	@InjectMocks
	private UsersController usersController = new UsersController(userFacade, roleResolver);

	private MockMvc mockMvc;

	private UserDTO userDTO1;
	private UserDTO userDTO2;
	private UserDTO userDTO3;

	@BeforeClass
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = standaloneSetup(usersController)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.setHandlerExceptionResolvers(createExceptionResolver())
				.build();
	}

	@BeforeMethod
	public void createEntities() {
		userDTO1 = new UserDTO();
		userDTO1.setId(1L);
		userDTO1.setFirstName("Lojzo");
		userDTO1.setSurname("Hrabovsky");
		userDTO1.setEmail("email@lojzo.com");

		userDTO2 = new UserDTO();
		userDTO2.setId(2L);
		userDTO2.setFirstName("Adam");
		userDTO2.setSurname("Vrtky");
		userDTO2.setEmail("email@adam.com");

		userDTO3 = new UserDTO();
		userDTO3.setId(3L);
		userDTO3.setFirstName("Lucia");
		userDTO3.setSurname("Mala");
		userDTO3.setEmail("email@lucia.com");
	}

	@BeforeMethod
	public void setUpResolver() {
		when(roleResolver.isSelf(any(), any())).thenReturn(false);
		when(roleResolver.hasRole(any(), any())).thenReturn(true);
	}

	@Test
	public void testGetAllUsers() throws Exception {
		List<UserDTO> users = new ArrayList<>();
		users.add(userDTO1);
		users.add(userDTO2);
		users.add(userDTO3);

		doReturn(Collections.unmodifiableList(users)).when(
				userFacade).getAllUsers();

		mockMvc.perform(get("/rest/auth/users"))
				.andExpect(status().isOk())
				.andExpect(
						content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(
						jsonPath("$.[?(@.id==1)].email").value("email@lojzo.com"))
				.andExpect(
						jsonPath("$.[?(@.id==2)].email").value("email@adam.com"))
				.andExpect(
						jsonPath("$.[?(@.id==3)].email").value("email@lucia.com"));

	}

	@Test
	public void testRegisterUser() throws Exception {
		UserDTO userDTO = new UserDTO();
		userDTO3.setFirstName("Jana");
		userDTO3.setSurname("Velka");
		userDTO3.setEmail("email@jana.com");

		String json = convertObjectToJsonBytes(userDTO);

		mockMvc.perform(
				post("/rest/auth/users/register?unencryptedPassword=1234").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(json))
				.andExpect(status().isOk());

	}

	@Test
	public void testIsAdmin() throws Exception {

		doReturn(false).when(userFacade).isAdmin(any(Long.class));

		mockMvc.perform(get("/rest/auth/users/isAdmin?id=1")).andExpect(content().string("false"));
	}

	@Test
	public void testRemoveAdmin() throws Exception {

		mockMvc.perform(put("/rest/auth/users/setAdmin?id=1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testSetAdmin() throws Exception {

		mockMvc.perform(put("/rest/auth/users/removeAdmin?id=1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testFindByEmail() throws Exception {

		when(userFacade.findUserByEmail("email@lucia.com")).thenReturn(userDTO3);

		mockMvc.perform(get("/rest/auth/users/filter?email=email@lucia.com"))
				.andExpect(jsonPath("$.[?(@.id==3)].email").value("email@lucia.com"));
	}

	@Test
	public void testFindById() throws Exception {

		when(userFacade.findUserById(1L)).thenReturn(userDTO1);

		mockMvc.perform(get("/rest/auth/users/1"))
				.andExpect(jsonPath("$.[?(@.id==1)].email").value("email@lojzo.com"));
	}

	@Test
	public void testDeleteUser() throws Exception {

		mockMvc.perform(delete("/rest/auth/users/1"))
				.andExpect(status().isOk());

		verify(userFacade, times(1)).deleteUser(1L);
	}

	private static String convertObjectToJsonBytes(Object object)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

	private ExceptionHandlerExceptionResolver createExceptionResolver() {
		ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
			protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
				Method method = new ExceptionHandlerMethodResolver(GlobalExceptionController.class).resolveMethod(exception);
				return new ServletInvocableHandlerMethod(new GlobalExceptionController(), method);
			}
		};
		exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		exceptionResolver.afterPropertiesSet();
		return exceptionResolver;
	}
}
