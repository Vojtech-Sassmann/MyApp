package cz.vectoun.myapp.facade;

import cz.vectoun.myapp.api.dto.UserAuthenticateDTO;
import cz.vectoun.myapp.api.dto.UserDTO;
import cz.vectoun.myapp.api.facade.UserFacade;
import cz.vectoun.myapp.persistance.entity.User;
import cz.vectoun.myapp.persistance.enums.UserRole;
import cz.vectoun.myapp.service.BeanMappingService;
import cz.vectoun.myapp.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Balcirak Peter <peter.balcirak@gmail.com>
 */
@Service
@Transactional
public class UserFacadeImpl implements UserFacade {

	private final UserService userService;

	private final BeanMappingService beanMappingService;

	@Inject
	public UserFacadeImpl(UserService userService, BeanMappingService beanMappingService) {
		this.userService = userService;
		this.beanMappingService = beanMappingService;
	}

	@Override
	public UserDTO findUserById(Long userId) {

		User user = userService.findById(userId);
		return (user == null) ? null : beanMappingService.mapTo(user, UserDTO.class);
	}

	@Override
	public void registerUser(UserDTO user, String unencryptedPassword) {
		User userEntity = beanMappingService.mapTo(user, User.class);
		userEntity.setRole(UserRole.REGULAR);
		userService.registerUser(userEntity, unencryptedPassword);
		user.setId(userEntity.getId());
	}

	@Override
	public List<UserDTO> getAllUsers() {
		return beanMappingService.mapTo(userService.getAll(), UserDTO.class);
	}

	@Override
	public boolean authenticate(UserAuthenticateDTO user) {
		return userService.authenticate(
				userService.findById(user.getUserId()), user.getPassword());
	}

	@Override
	public boolean isAdmin(Long userId) {
		return userService.isAdmin(userService.findById(userId));
	}

	@Override
	public void setAdmin(Long userId) {
		userService.setAdmin(userService.findById(userId));
	}

	@Override
	public void removeAdmin(Long userId) {
		userService.removeAdmin(userService.findById(userId));
	}

	@Override
	public void deleteUser(Long userId) {
		userService.delete(userService.findById(userId));
	}
}
