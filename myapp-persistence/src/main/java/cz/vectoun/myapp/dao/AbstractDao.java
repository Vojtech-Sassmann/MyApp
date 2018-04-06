package cz.vectoun.myapp.dao;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Sassmann Vojtech <vojtech.sassmann@gmail.com>
 * @param <T> Class of the entity
 */
@Named
public abstract class AbstractDao<T> implements Dao<T> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(T entity) {
        if(entity == null){
            throw new IllegalArgumentException("Parameter entity cannot be null!");
        }
        em.persist(entity);
    }

    @Override
    public void delete(T entity) {
        if(entity == null){
            throw new IllegalArgumentException("Parameter entity cannot be null!");
        }
        em.remove(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(Long id) {
        if(id == null){
            throw new IllegalArgumentException("Parameter id cannot be null!");
        }
        if(id < 0){
            throw new IllegalArgumentException("Parameter id cannot be less than 0!");
        }
        return (T)em.find(getGenericClass(), id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return (List<T>) em.createQuery("SELECT e FROM " + getGenericClass().getName() + " e",
                getGenericClass()).getResultList();
    }

    private Class<?> getGenericClass() {
        Class<?> result = null;
        Type type = this.getClass().getGenericSuperclass();

        if(type instanceof ParameterizedType){
            ParameterizedType pt =(ParameterizedType) type;
            Type[] fieldArgTypes = pt.getActualTypeArguments();
            result =(Class<?>) fieldArgTypes[0];
        }
        return result;
    }
}
