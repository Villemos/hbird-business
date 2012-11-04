package org.hbird.exchange.core;

/**
 * @author Admin
 *
 * A Label is a String value used to denote something. It can for example be a 
 * calibrated value describing something as 'ON' or 'OFF'.
 *
 */
public class Label extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2714648024647654679L;

	/** The label value. */
	protected String value = "";

	/**
	 * Creates a Parameter with a timestamp set to 'now'.
	 * 
	 * @param issuedBy The name of the source of this label.
	 * @param name The name of the label
	 * @param type The Ontology type of this object. Is not 'String', but for example 'Calibration'.
	 * @param description A description of the label.
	 * @param value An object holding the value of the label.
	 */
	public Label(String issuedBy, String name, String type, String description, String value) {
		super(issuedBy, name, type, description);
		this.value = value;
	}

	public Label(Label base) {
		this(base.issuedBy, base.name, base.type, base.description, base.value);
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String prettyPrint() {
		return "Label {name=" + name + ", value=" + value + ", timestamp=" + timestamp + "}";
	}

}
