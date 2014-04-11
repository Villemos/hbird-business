package org.hbird.exchange.core;

import org.springframework.beans.BeanUtils;

/**
 * A CALIBRATED NUMERICAL parameter. Extends <code>Parameter</code>, also holds
 * raw data of the <code>Parameter</code>.
 * 
 */
public class CalibratedParameter extends Parameter {

    /** The unique UID. */
    private static final long serialVersionUID = 102301904839710474L;

    /** Raw (uncalibrated) value of the parameter. May be any type. */
    protected Number rawValue;

    /** The unit of the raw (uncalibrated) value. */
    protected String rawUnit;

    /**
     * Creates a <code>CalibratedParameter</code> with a timestamp set to 'now'.
     * 
     * @param ID
     *        The ID of the parameter
     * @param name
     *        The name of the parameter
     */
    public CalibratedParameter(String ID, String name) {
        super(ID, name);
    }

    /**
     * Creates a <code>CalibratedParameter</code> with a timestamp set to 'now'.
     * 
     * @param ID
     *        The ID of the parameter
     * @param name
     *        The name of the parameter
     */
    public CalibratedParameter(Parameter p) {
        super(p.ID, p.name);
        BeanUtils.copyProperties(p, this);
        this.rawValue = value;
        this.rawUnit = unit;
    }

    /**
     * Returns the raw value of the <code>CalibratedParameter</code> as a
     * <code>Number</code>.
     * 
     * @return An <code>Object</code> holding the value. The class type can of
     *         cause be found
     *         using reflection, or through the 'clazz' attribute.
     */
    public Number getRawValue() {
        return rawValue;
    }

    public void setRawDoubleValue(double value) {
        this.rawValue = value;
    }

    public double rawAsDouble() {
        return rawValue.doubleValue();
    }

    public int rawAsInt() {
        return rawValue.intValue();
    }

    /**
     * Setter for the raw value. Will also set the 'clazz' attribute to
     * the Java
     * name of the class set as value.
     * 
     * @param value
     *        The value to be set.
     */
    public void setRawValue(Number value) {
        this.rawValue = value;
    }

    /**
     * Returns the string representing the unit of this raw parameter.
     * 
     * @return String representing the unit.
     */
    public String getRawUnit() {
        return unit;
    }

    /**
     * @param unit
     *        the unit to set
     */
    public void setRawUnit(String unit) {
        this.unit = unit;
    }

}
