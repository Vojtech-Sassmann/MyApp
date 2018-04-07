package cz.vectoun.myapp.sampledata;

import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.persistance.enums.UserRole;
import cz.vectoun.myapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
@Service
@Transactional
public class SampleDataLoadingFacadeImpl implements SampleDataLoadingFacade {

	private final UserService userService;

	@Inject
	public SampleDataLoadingFacadeImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void loadUserData() {
		User user1 = new User();
		User user2 = new User();
		User user3 = new User();

		user1.setFirstName("Alojz");
		user1.setSurname("Hlinka");
		user1.setEmail("regular@regular.cz");
		user1.setRole(UserRole.REGULAR);

		user2.setFirstName("Jozef");
		user2.setSurname("Mak");
		user2.setEmail("jozef@mak.com");
		user2.setRole(UserRole.REGULAR);

		user3.setFirstName("Lucia");
		user3.setSurname("Srnova");
		user3.setEmail("admin@admin.cz");
		user3.setRole(UserRole.ADMIN);

		userService.registerUser(user1, "regular");
		userService.registerUser(user2, "1234");
		userService.registerUser(user3, "admin");
	}
}
