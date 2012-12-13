package org.hbird.business.configurator;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.navigation.KeplianOrbitPredictor;
import org.hbird.business.navigation.TleOrbitalPredictor;
import org.hbird.exchange.navigation.TlePropagationRequest;


public class NavigationComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {

		TleOrbitalPredictor tlePredictor = new TleOrbitalPredictor();
		KeplianOrbitPredictor keplianOrbitPredictor = new KeplianOrbitPredictor();
		tlePredictor.setKeplianOrbitPredictor(keplianOrbitPredictor);

		/** Create the route and the interface for getting the Locations. */
		from("direct:locationstore").to("solr:locationstore");
		
		/** Route for commands to this component, requests for predictions. */
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName()))
		.choice()
		.when(body().isInstanceOf(TlePropagationRequest.class))
		.bean(tlePredictor)
		.otherwise()
		.bean(keplianOrbitPredictor)
		.end();
		
		ProcessorDefinition route = from("direct:navigationinjection");
		addInjectionRoute(route);

	}
}
