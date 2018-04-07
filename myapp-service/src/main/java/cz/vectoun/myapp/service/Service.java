package cz.vectoun.myapp.service;

import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface Service<T> {

    /**
     * Finds entity by its Id
     * @param id id
     *
     * @return found entity or null
     */
    T findById(Long id);

    /**
     * Finds all entities
     *
     * @return all entities
     */
    List<T> getAll();

    /**
     * Deletes given entity
     *
     * @param entity to be deleted
     */
    void delete(T entity);
}
