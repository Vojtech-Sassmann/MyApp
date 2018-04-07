package cz.vectoun.myapp.persistance.dao;


import cz.vectoun.myapp.persistance.entity.User;

/**
 * Interface of UserDao
 * @author Sassmann Vojtech <vojtech.sassmann@gmail.com>
 */
public interface UserDao extends Dao<User> {

	/**
	 * Finds user in database by his email and returns him as object User
	 * @param email by which will be user find
	 * @return User or null if there is not user whit that email
	 *
	 * @throws IllegalArgumentException when given parameter is null or empty
	 */
	User findByEmail(String email);
}
