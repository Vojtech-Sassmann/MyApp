package cz.vectoun.myapp.persistance.dao;

import cz.vectoun.myapp.persistance.entity.NoteGroup;
import cz.vectoun.myapp.persistance.entity.User;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
@Named
public class NoteGroupDaoImpl extends AbstractDao<NoteGroup> implements NoteGroupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<NoteGroup> findAllForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User can not be null.");
        }

        return em.createQuery("select n from NoteGroup n where n.owner = :user", NoteGroup.class)
                .setParameter("user", user)
                .getResultList();
    }
}
