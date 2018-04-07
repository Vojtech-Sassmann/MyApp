package cz.vectoun.myapp.service;


import cz.vectoun.myapp.persistance.entity.User;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface UserService extends Service<User> {

    /**
     * Register the given user with the given unencrypted password.
     *
     * @param user which will be registered
     * @param unencryptedPassword by which will be user authenticated
     */
    void registerUser(User user, String unencryptedPassword);

    /**
     * Try to authenticate a user.
     *
     * @param user which will be authenticated
     * @param password given password
     * @return true only if the hashed password matches the records, false otherwise
     */
    boolean authenticate(User user, String password);

    /**
     * Check if the given user is admin.
     *
     * @param user which will be checked
     * @return true if user is Admin, false otherwise
     */
    boolean isAdmin(User user);

    /**
     * Set user role to admin.
     *
     * @param user which will be set to Admin
     */
    void setAdmin(User user);

    /**
     * Set user role to regular user.
     *
     * @param user which will be set to regular user
     */
    void removeAdmin(User user);
}
