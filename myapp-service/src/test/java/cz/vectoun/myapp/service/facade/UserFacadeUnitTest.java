package cz.vectoun.myapp.service.facade;

import cz.vectoun.myapp.api.dto.UserAuthenticateDTO;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.facade.UserFacade;
import cz.vectoun.myapp.facade.UserFacadeImpl;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.persistance.enums.UserRole;
import cz.vectoun.myapp.service.BeanMappingService;
import cz.vectoun.myapp.service.UserService;
import cz.vectoun.myapp.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class UserFacadeUnitTest extends AbstractTestNGSpringContextTests {

	private UserService userService = mock(UserService.class);

	//TODO: change this to mocked object
	@Inject
	private BeanMappingService beanMappingService;

	@InjectMocks
	private UserFacade userFacade;

	private User user1;
	private User user2;
	private User user3;
	private UserDTO userDTO1;
	private UserDTO userDTO2;
	private UserDTO userDTO3;
	private UserAuthenticateDTO userAuthDTO1;
	private UserAuthenticateDTO userAuthDTO2;
	private UserAuthenticateDTO userAuthDTO3;

	@BeforeMethod
	public void setFacade() {
		userFacade = new UserFacadeImpl(userService, beanMappingService);
	}

	@BeforeMethod
	public void createEntities() {
		user1 = new User();
		user1.setFirstName("Lojzo");
		user1.setSurname("Hrabovsky");
		user1.setEmail("email@lojzo.com");
		user1.setRole(UserRole.REGULAR);

		user2 = new User();
		user2.setFirstName("Adam");
		user2.setSurname("Vrtky");
		user2.setEmail("email@adam.com");
		user2.setRole(UserRole.REGULAR);

		user3 = new User();
		user3.setFirstName("Lucia");
		user3.setSurname("Mala");
		user3.setEmail("email@lucia.com");
		user3.setRole(UserRole.REGULAR);

		userDTO1 = new UserDTO();
		userDTO1.setFirstName("Lojzo");
		userDTO1.setSurname("Hrabovsky");
		userDTO1.setEmail("email@lojzo.com");
		userDTO1.setRole(cz.vectoun.myapp.api.enums.UserRole.REGULAR);

		userDTO2 = new UserDTO();
		userDTO2.setFirstName("Adam");
		userDTO2.setSurname("Vrtky");
		userDTO2.setEmail("email@adam.com");
		userDTO2.setRole(cz.vectoun.myapp.api.enums.UserRole.REGULAR);

		userDTO3 = new UserDTO();
		userDTO3.setFirstName("Lucia");
		userDTO3.setSurname("Mala");
		userDTO3.setEmail("email@lucia.com");
		userDTO3.setRole(cz.vectoun.myapp.api.enums.UserRole.REGULAR);

		userAuthDTO1 = new UserAuthenticateDTO();
		userAuthDTO1.setPassword("0000");
		userAuthDTO1.setUserId(1L);

		userAuthDTO2 = new UserAuthenticateDTO();
		userAuthDTO2.setPassword("1234");
		userAuthDTO2.setUserId(2L);

		userAuthDTO3 = new UserAuthenticateDTO();
		userAuthDTO3.setPassword("1111");
		userAuthDTO3.setUserId(3L);
	}

	@Test
	public void registerUserTest() {
		userFacade.registerUser(userDTO1, "123456789");

		verify(userService, times(1)).registerUser(user1, "123456789");
	}

	@Test
	public void deleteUserTest() {
		when(userService.findById(any(Long.class))).thenReturn(user2);

		userFacade.deleteUser(userDTO2.getId());

		verify(userService, times(1)).delete(user2);
	}

	@Test
	public void getAllUsersTest() {

		when(userService.getAll()).thenReturn(Arrays.asList(user1, user2, user3));

		List<UserDTO> allUsers = userFacade.getAllUsers();
		assertThat(allUsers).containsOnly(userDTO1, userDTO2, userDTO3);
	}

	@Test
	public void FindByIdTest() {
		when(userService.findById(any(Long.class))).thenReturn(user1);

		UserDTO foundUser = userFacade.findUserById(1L);

		assertThat(foundUser).isEqualToComparingFieldByField(userDTO1);
	}

	@Test
	public void authenticateTest() {
		when(userService.authenticate(any(User.class), any(String.class))).thenReturn(true);

		boolean answer = userFacade.authenticate(userAuthDTO3);

		assertThat(answer).isEqualTo(true);
	}

	@Test
	public void isAdminTest() {
		userDTO1.setRole(cz.vectoun.myapp.api.enums.UserRole.ADMIN);

		when(userService.findById(any(Long.class))).thenReturn(user1);
		when(userService.isAdmin(any(User.class))).thenReturn(true);

		boolean answer = userFacade.isAdmin(userDTO1.getId());

		assertThat(answer).isEqualTo(true);
	}

	@Test
	public void setAdminTest() {
		when(userService.findById(any(Long.class))).thenReturn(user3);

		userFacade.setAdmin(userDTO3.getId());

		verify(userService, times(1)).setAdmin(user3);
	}

	@Test
	public void removeAdminTest() {
		userDTO1.setRole(cz.vectoun.myapp.api.enums.UserRole.ADMIN);
		user1.setRole(cz.vectoun.myapp.persistance.enums.UserRole.ADMIN);

		when(userService.findById(any(Long.class))).thenReturn(user1);

		userFacade.setAdmin(userDTO1.getId());

		verify(userService, times(1)).setAdmin(user1);
	}
}
