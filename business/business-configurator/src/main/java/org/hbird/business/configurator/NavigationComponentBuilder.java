package org.hbird.business.configurator;

import java.util.List;

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.navigation.KeplianOrbitPredictor;
import org.hbird.business.navigation.OrbitPredictor;
import org.hbird.business.navigation.OrbitPropagator;
import org.hbird.business.navigation.TleOrbitalPredictor;
import org.hbird.exchange.navigation.Location;


public class NavigationComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		OrbitPropagator propagator = new OrbitPropagator();
		
		String predictortype = (String) request.getArguments().get("predictortype");
		
		OrbitPredictor predictor = null;
		if (predictortype.equals("keplian")) {
			predictor = new KeplianOrbitPredictor();
		}
		else if (predictortype.equals("tle")) {
			predictor = new TleOrbitalPredictor();
		}

		predictor.setName(getComponentName());

		if (request.getArguments().containsKey("locations")) {
			for (Location location : (List<Location>) request.getArguments().get("locations")) {
				propagator.addLocation(location);
			}
		}

		/** Route for commands to this component, i.e. configuration commands. */
		ProcessorDefinition route = from("seda:processCommandFor" + getComponentName())
				.bean(predictor)
				.split(body());
		addInjectionRoute(route);
		
		from(StandardEndpoints.commands + "?" + addDestinationSelector(getComponentName())).bean(predictor, "predictOrbit");
	}
}
