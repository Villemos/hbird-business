package org.hbird.business.navigation;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.hbird.exchange.navigation.OrbitPredictionRequest;
import org.hbird.exchange.navigation.OrbitalState;
import org.orekit.errors.OrekitException;

public abstract class OrbitPredictor {

	protected String name = "OrbitPredictor";

	/** FIXME I don't know what this does but OREKIT needs it...*/
	protected double maxcheck = 1.;

	/** gravitation coefficient */
	protected Double mu = 3.986004415e+14; 

	/** flattening */
	protected Double f  =  1.0 / 298.257223563; 

	/** equatorial radius in meter */
	protected Double ae = 6378137.0; 

	protected List<Object> results = new ArrayList<Object>();

	public void addResult(Object result) {
		this.results.add(result);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	abstract public List<Object> predictOrbit(@Body OrbitPredictionRequest request) throws OrekitException;
}
