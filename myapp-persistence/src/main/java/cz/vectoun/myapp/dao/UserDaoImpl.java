package cz.vectoun.myapp.dao;

import cz.vectoun.myapp.entity.User;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * Implementation of UserDao interface
 * @author Sassmann Vojtech <vojtech.sassmann@gmail.com>
 */
@Named
public class UserDaoImpl extends AbstractDao<User> implements UserDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public User findByEmail(String email) {
		if(email == null){
			throw new IllegalArgumentException("Parameter email cannot be null!");
		}
		if(email.isEmpty()){
			throw new IllegalArgumentException("Parameter email cannot be empty!");
		}
		try {
			return em
					.createQuery("select u from User u where email = :email",
							User.class).setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException nrf) {
			return null;
		}
	}
}

