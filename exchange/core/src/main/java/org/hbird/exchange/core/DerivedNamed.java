package org.hbird.exchange.core;

public class DerivedNamed extends Named implements IDerived {

	/**
	 * 
	 */
	private static final long serialVersionUID = -585663387325032094L;

	protected NamedInstanceIdentifier from = null;
	
	/**
	 * Constructor of a Named object. The timestamp will be set to the creation time.
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 */
	public DerivedNamed(String issuedBy, String name, String type, String description, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, name, type, description);
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;

		this.from = new NamedInstanceIdentifier(derivedFromName, derivedFromTimestamp, derivedFromType);
	}

	/**
	 * Constructor of a Named object with a specific timestamp. 
	 * 
	 * @param name The name of the object.
	 * @param description The description of the object.
	 * @param timestamp The timestamp of the object.
	 */
	public DerivedNamed(String issuedBy, String name, String type, String description, long timestamp, String derivedFromName, long derivedFromTimestamp, String derivedFromType) {
		super(issuedBy, name, type, description);
		this.issuedBy = issuedBy;
		this.name = name;
		this.type = type;
		this.description = description;
		this.timestamp = timestamp;

		this.from = new NamedInstanceIdentifier(derivedFromName, derivedFromTimestamp, derivedFromType);
	}
	
	public NamedInstanceIdentifier from() {
		return from;
	}
}
