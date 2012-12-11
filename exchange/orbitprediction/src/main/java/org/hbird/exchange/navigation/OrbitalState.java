package org.hbird.exchange.navigation;

import org.hbird.exchange.core.Named;

public abstract class OrbitalState extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8777084460832347503L;
	
	public Satellite satelitte = null;
	
	/**
	 * Constructor of an orbital state.
	 * 
	 * @param name The name of the orbital state. 
	 * @param description A description of the state.
	 * @param timestamp The timestamp at which the orbital state is relevant.
	 * @param datasetidentifier
	 * @param satellite 
	 * @param position The position of the orbit. 
	 * @param velocity The velocity of the orbit.
	 */
	public OrbitalState(String issuedBy, String name, String description, long timestamp, String datasetidentifier, Satellite satellite) {
		super(issuedBy, name, "Orbital State", description, timestamp, datasetidentifier);
		this.satelitte = satellite;
	}

	public OrbitalState(Satellite satellite) {
		this.satelitte = satellite;
	}
	
	public Satellite getSatelitte() {
		return satelitte;
	}
}
