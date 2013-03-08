package org.hbird.business.core.naming;

public enum Base {

	
	SATELLITE("Satellite"),
	GROUND_STATION("GroundStation"),
	WEATHER_STATION("WeatherStation"),
	HOST("Host"),
	;

	private final String name;

	private Base(String name) {
		this.name = name;
	}

	/** @{inheritDoc . */
	@Override
	public String toString() {
		return name;
	}
}
