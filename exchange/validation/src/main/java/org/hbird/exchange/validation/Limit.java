package org.hbird.exchange.validation;

import org.hbird.exchange.core.Named;

public class Limit extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4304941431961981299L;

	public Limit(eLimitType type, String ofParameter, Object value, String stateName, String description) {
		super("", stateName, description);
		
		this.type = type;
		this.parameter = ofParameter;
		this.limit = value;
	}

	public Limit(eLimitType type, String ofParameter, String value, eValueType valuetype, String stateName, String description) {
		super("", stateName, description);
		
		this.type = type;
		this.parameter = ofParameter;
		
		if (valuetype == eValueType.Integer) {
			this.limit = Integer.parseInt(value);
		}
		else if (valuetype == eValueType.Double) {
			this.limit = Double.parseDouble(value);
		}
		else if (valuetype == eValueType.Float) {
			this.limit = Float.parseFloat(value);
		}
		else if (valuetype == eValueType.Boolean) {
			this.limit = Boolean.parseBoolean(value);
		}
		else {
			this.limit = value;
		}
	}
	
	/** The type of the limit. The type can be;
	 * - Lower. The limit is violated if the parameter value falls BELOW the limit. 
	 * - Upper. The limit is violated if the parameter value goes ABOVE the limit.
	 * - Static. The limit is violated if it has any other value than the defined. 
	 */
	public enum eLimitType {Lower, Upper, Static};
	public eLimitType type = eLimitType.Lower;
	
	public enum eValueType {Double, Integer, Float, String, Boolean};
	
	/** Flag indicating whether the limit is enabled or not. If set to 'null' or 'false' then
	 * the limit shall not be evaluated. */
	public Boolean enabled = true;

	/** The limit value to be checked against. */
	public Object limit = 0d;

	/** The number of points after the comma to which the value must be accurate. Can be used 
	 * in particular with STATIC limits, where absolutely equal may be too hard a constrain. */
	public int accuracy = 32;

	/** The name of the parameter being checker. */
	public String parameter = null;

}
