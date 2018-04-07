package cz.vectoun.myapp.service;

import cz.vectoun.myapp.persistance.dao.Dao;

import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public abstract class AbstractService<T> implements Service<T> {

    private Dao<T> dao;

    protected AbstractService(Dao<T> dao) {
        this.dao = dao;
    }

    @Override
    public T findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<T> getAll() {
        return dao.getAll();
    }

    @Override
    public void delete(T entity) {
        dao.delete(entity);
    }
}
