package org.hbird.business.configurator;

import org.hbird.business.navigation.OrbitPredictor;
import org.hbird.business.navigation.OrbitPropagator;

public class NavigationComponentBuilder extends ComponentBuilder {

	@Override
	public void doConfigure() {
		OrbitPropagator propagator = new OrbitPropagator();
		OrbitPredictor predictor = new OrbitPredictor();
				
		from("activemq:topic:orbitals").bean(propagator, "propagate").to("activemq:queue:requests");
		from("activemq:topic:configuration").bean(propagator, "addLocation");
		from("activemq:topic:configuration").bean(propagator, "removeLocation");

		from("activemq:queue:requests")
		.bean(predictor)
		.split(body())
		.setHeader("issuedBy", simple("${in.body.issuedBy}"))
		.setHeader("name", simple("${in.body.name}"))
		.setHeader("type", simple("${in.body.type}"))
		.to("activemq:topic:monitoringdata");
	}
}
