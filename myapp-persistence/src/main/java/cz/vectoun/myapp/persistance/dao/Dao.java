package cz.vectoun.myapp.persistance.dao;

import java.util.List;

/**
 * @author Sassmann Vojtech <vojtech.sassmann@gmail.com>
 */
public interface Dao<T> {
	/**
	 * Creates entity object in database
	 * @param entity which will be created
	 *
	 * @throws IllegalArgumentException when given parameter is null
	 */
	void create(T entity);

	/**
	 * Deletes entity from database
	 * @param entity which will be deleted
	 *
	 * @throws IllegalArgumentException when given parameter is null
	 */
	void delete(T entity);

	/**
	 * Finds entity in database by its id and returns it
	 * @param id by which will be entity found
	 * @return entity or null if there is not entity with that id
	 *
	 * @throws IllegalArgumentException when given parameter is null or less than 0
	 */
	T findById(Long id);

	/**
	 * Gets all entities of T stored in database
	 * @return List of all entity of T in database or empty List if there are none
	 */
	List<T> getAll();
}
