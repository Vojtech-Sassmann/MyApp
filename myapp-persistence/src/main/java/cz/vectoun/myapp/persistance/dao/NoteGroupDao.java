package cz.vectoun.myapp.persistance.dao;

import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;

import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface NoteGroupDao extends Dao<NoteGroup> {

    /**
     * Finds all note groups for given user.
     *
     * @param user user
     * @return list of all note groups for given user
     */
    List<NoteGroup> findAllForUser(User user);
}
