package cz.vectoun.myapp.service.config;

import cz.vectoun.myapp.persistance.PersistenceApplicationContext;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import(PersistenceApplicationContext.class)
@ComponentScan(basePackages = "cz.vectoun.myapp.service")
public class ServiceConfiguration {

	@Bean
	public Mapper dozer() {
		DozerBeanMapper dozer = new DozerBeanMapper();

		List<String> mappingFiles = new ArrayList<>();
		mappingFiles.add("dozerJdk8Converters.xml");
		dozer.setMappingFiles(mappingFiles);

		return dozer;
	}

}
