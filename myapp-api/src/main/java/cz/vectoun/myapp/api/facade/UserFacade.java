package cz.vectoun.myapp.api.facade;


import cz.vectoun.myapp.api.dto.UserAuthenticateDTO;
import cz.vectoun.myapp.api.dto.UserDTO;

import java.util.List;

/**
 * Facade interface for UserDTO entities.
 *
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface UserFacade {

	/**
	 * Find user with the given id in database
	 *
	 * @param userId by which will be user find
	 * @return UserDTO object with that id
	 */
	UserDTO findUserById(Long userId);

	/**
	 * Find user with the given email in database
	 *
	 * @param email by which will be user find
	 * @return UserDTO object with that email
	 */
	UserDTO findUserByEmail(String email);

	/**
	 * Register the given user with the given unencrypted password.
	 *
	 * @param user which will be registered
	 * @param unencryptedPassword by which will be user authenticate
	 */
	void registerUser(UserDTO user, String unencryptedPassword);

	/**
	 * Get all registered users
	 *
	 * @return List of UserDTO objects
	 */
	List<UserDTO> getAllUsers();

	/**
	 * Try to authenticate a user.
	 *
	 * @param user which will be authenticated
	 * @return true only if the hashed password matches the records, false otherwise
	 */
	boolean authenticate(UserAuthenticateDTO user);

	/**
	 * Check if the given user is admin.
	 *
	 * @param userId by which will be user checked
	 * @return true if user is Admin, false otherwise
	 */
	boolean isAdmin(Long userId);

	/**
	 * Set user role to admin.
	 *
	 * @param userId by which will be user set to Admin
	 */
	void setAdmin(Long userId);

	/**
	 * Set user role to regular user.
	 *
	 * @param userId by which will be user set to regular user
	 */
	void removeAdmin(Long userId);

	/**
	 * delete user from database by his id.
	 *
	 * @param userId by which will be user deleted
	 */
	void deleteUser(Long userId);
}
