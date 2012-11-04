package org.hbird.exchange.validation;

import org.hbird.exchange.core.Parameter;

public class Limit extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4304941431961981299L;

	public Limit(eLimitType type, String ofParameter, Number value, String stateName, String description) {
		super("", stateName, "Limit", description, value, "");
		
		this.type = type;
		this.limitOfParameter = ofParameter;
	}

	/** The type of the limit. The type can be;
	 * - Lower. The limit is violated if the parameter value falls BELOW the limit. 
	 * - Upper. The limit is violated if the parameter value goes ABOVE the limit.
	 * - Static. The limit is violated if it has any other value than the defined. 
	 */
	public enum eLimitType {Lower, Upper, Static};
	public eLimitType type = eLimitType.Lower;
	
	public Boolean enabled = true;
	
	/** The number of points after the comma to which the value must be accurate. Can be used 
	 * in particular with STATIC limits, where absolutely equal may be too hard a constrain. */
	public int accuracy = 32;

	/** The name of the parameter being checker. */
	public String limitOfParameter = null;

}
