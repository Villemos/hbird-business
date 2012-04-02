package org.hbird.exchange.navigation;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.ServiceSpecification;

public class NavigationServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7691491162885870668L;

	{
		this.name = "";
		this.description = "";

		List<Object> configuration = new ArrayList<Object>();
		configuration.add(new Location());
		configuration.add(new Satelitte());
		configuration.add(new OrbitalState());
		this.logicalIn.put("configuration", configuration);
		this.logicalIn.put("request", new OrbitPredictionRequest());

		this.logicalOut.put("results", new OrbitalState());
	}

}
