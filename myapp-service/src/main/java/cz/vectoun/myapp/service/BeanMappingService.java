package cz.vectoun.myapp.service;

import org.dozer.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface BeanMappingService {

	<T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

	<T> T mapTo(Object o, Class<T> mapToClass);

	Mapper getMapper();
}
