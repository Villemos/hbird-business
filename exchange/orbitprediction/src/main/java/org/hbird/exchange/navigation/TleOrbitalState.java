package org.hbird.exchange.navigation;

public class TleOrbitalState extends OrbitalState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2247881108684516270L;

	public String tleLine1;
	public String tleLine2;

	public String uploaderName; 
	
	public TleOrbitalState(String issuedBy, String name, String description, long timestamp, String datasetidentifier, Satellite satellite, String tleLine1, String tleLine2) {
		super(issuedBy, name, description, timestamp, datasetidentifier, satellite);
		
		this.tleLine1 = tleLine1;
		this.tleLine2 = tleLine2;		
	}
	
	public TleOrbitalState(String issuedBy, String name, String description, long timestamp, Satellite satellite, String tleLine1, String tleLine2) {
		super(issuedBy, name, description, timestamp, "", satellite);
		
		this.tleLine1 = tleLine1;
		this.tleLine2 = tleLine2;		
	}

}
