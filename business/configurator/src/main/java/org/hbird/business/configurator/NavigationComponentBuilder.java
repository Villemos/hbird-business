package org.hbird.business.configurator;

import org.hbird.business.navigation.OrbitPredictor;
import org.hbird.business.navigation.OrbitPropagator;
import org.hbird.exchange.navigation.NavigationServiceSpecification;

public class NavigationComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		card.provides.add(new NavigationServiceSpecification());
		
		OrbitPropagator propagator = new OrbitPropagator();
		OrbitPredictor predictor = new OrbitPredictor();
				
		from("activemq:topic:orbitals").bean(propagator, "propagate").to("activemq:queue:requests");
		from("activemq:topic:configuration").bean(propagator, "addLocation");
		from("activemq:topic:configuration").bean(propagator, "removeLocation");

		from("activemq:queue:requests").bean(predictor).split(body()).to("activemq:topic:orbitals");
	}
}
