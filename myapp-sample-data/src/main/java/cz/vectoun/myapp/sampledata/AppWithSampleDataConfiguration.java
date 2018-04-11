package cz.vectoun.myapp.sampledata;

import cz.vectoun.myapp.service.config.ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
@Configuration
@Import(ServiceConfiguration.class)
@ComponentScan(basePackageClasses = {SampleDataLoadingFacadeImpl.class})
public class AppWithSampleDataConfiguration {

	private final SampleDataLoadingFacade sampleDataLoadingFacade;

	@Inject
	public AppWithSampleDataConfiguration(SampleDataLoadingFacade sampleDataLoadingFacade) {
		this.sampleDataLoadingFacade = sampleDataLoadingFacade;
	}

	@PostConstruct
	public void dataLoading() {
		sampleDataLoadingFacade.loadData();
	}
}
