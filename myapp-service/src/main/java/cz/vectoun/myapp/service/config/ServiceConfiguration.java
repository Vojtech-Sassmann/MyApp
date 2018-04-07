package cz.vectoun.myapp.service.config;

import cz.vectoun.myapp.persistance.PersistenceApplicationContext;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PersistenceApplicationContext.class)
@ComponentScan(basePackages = "cz.vectoun.myapp.service")
public class ServiceConfiguration {

	@Bean
	public Mapper dozer() {
		return new DozerBeanMapper();
	}
}
