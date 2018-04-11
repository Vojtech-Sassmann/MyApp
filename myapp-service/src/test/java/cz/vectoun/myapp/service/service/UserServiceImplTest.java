package cz.vectoun.myapp.service.service;

import cz.vectoun.myapp.service.NoteGroupService;
import cz.vectoun.myapp.service.NoteService;
import cz.vectoun.myapp.service.UserService;
import cz.vectoun.myapp.service.UserServiceImpl;
import cz.vectoun.myapp.persistance.dao.UserDao;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.persistance.enums.UserRole;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
public class UserServiceImplTest {

    private UserDao userDao = mock(UserDao.class);
    private NoteGroupService noteGroupService = mock(NoteGroupService.class);
    private NoteService noteService = mock(NoteService.class);

    private User user1;
    private User user2;
    private User user3;

    @InjectMocks
    private UserService userService;

    @BeforeMethod
    public void setService() {
        userService = new UserServiceImpl(userDao, noteGroupService, noteService);
    }

    @BeforeMethod
    public void createEntities() {
        user1 = new User();
        user1.setFirstName("Lojzo");
        user1.setSurname("Hrabovsky");
        user1.setEmail("email@lojzo.com");

        user2 = new User();
        user2.setFirstName("Adam");
        user2.setSurname("Vrtky");
        user2.setEmail("email@adam.com");

        user3 = new User();
        user3.setFirstName("Lucia");
        user3.setSurname("Mala");
        user3.setEmail("email@lucia.com");
    }

    @Test
    public void registerUserTest() {
        userService.registerUser(user1, "123456789");

        verify(userDao, times(1)).create(user1);
    }

    @Test
    public void deleteUserTest() {
        userService.delete(user2);

        verify(userDao, times(1)).delete(user2);
    }

    @Test
    public void getAllUsersTest() {

        when(userDao.getAll()).thenReturn(Arrays.asList(user1, user2, user3));

        List<User> allUsers = userService.getAll();
        assertThat(allUsers).containsOnly(user1, user2, user3);
    }

    @Test
    public void FindByIdTest() {
        when(userDao.findById(1L)).thenReturn(user1);

        User foundUser = userService.findById(1L);

        assertThat(foundUser).isEqualToComparingFieldByField(user1);
    }

    @Test
    public void FindByIdNotFoundTest() {
        when(userDao.findById(1L)).thenReturn(null);

        User foundUser = userService.findById(1L);

        assertThat(foundUser).isEqualTo(null);
    }

    @Test
    public void authenticateTest() {
        userService.registerUser(user3, "123456789");
        boolean answer = userService.authenticate(user3,"123456789");
        assertThat(answer).isEqualTo(true);
    }

    @Test
    public void authenticateIncorrectPasswordTest() {
        userService.registerUser(user3, "123456789");
        boolean answer = userService.authenticate(user3,"12345");
        assertThat(answer).isEqualTo(false);
    }

    @Test
    public void isAdminTest() {
        user1.setRole(UserRole.ADMIN);

        when(userDao.findById(any(Long.class))).thenReturn(user1);

        boolean answer = userService.isAdmin(user1);

        assertThat(answer).isEqualTo(true);
    }

    @Test
    public void isAdminNotTrueTest() {
        user1.setRole(UserRole.REGULAR);

        when(userDao.findById(any(Long.class))).thenReturn(user1);

        boolean answer = userService.isAdmin(user1);

        assertThat(answer).isEqualTo(false);
    }

    @Test
    public void setAdminTest() {

        when(userDao.findById(any(Long.class))).thenReturn(user1);

        userService.setAdmin(user1);

        assertThat(user1.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    public void setAdminAlreadyAdminTest() {
        user1.setRole(UserRole.ADMIN);

        when(userDao.findById(any(Long.class))).thenReturn(user1);

        userService.setAdmin(user1);

        assertThat(user1.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    public void removeAdminTest() {
        user1.setRole(UserRole.ADMIN);

        when(userDao.findById(any(Long.class))).thenReturn(user1);

        userService.removeAdmin(user1);

        assertThat(user1.getRole()).isEqualTo(UserRole.REGULAR);
    }

    @Test
    public void removeAdminAlreadyRegularTest() {
        user1.setRole(UserRole.REGULAR);

        when(userDao.findById(any(Long.class))).thenReturn(user1);

        userService.removeAdmin(user1);

        assertThat(user1.getRole()).isEqualTo(UserRole.REGULAR);
    }
}
