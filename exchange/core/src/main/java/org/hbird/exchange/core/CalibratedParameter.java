package org.hbird.exchange.core;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

/**
 * A CALIBRATED NUMERICAL parameter. Extends <code>Parameter</code>, holds
 * calibrated data.
 * 
 */
public class CalibratedParameter extends Parameter {

    /** The unique UID. */
    private static final long serialVersionUID = 102301904839710474L;

    /** Raw calibrated value of the parameter. May be any type. */
    protected Number value;

    /** The unit of the calibrated value. */
    protected String unit;

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
     * @param parameter raw parameter
     */
    public CalibratedParameter(Parameter parameter) {
        super(parameter.ID, parameter.name);
        BeanUtils.copyProperties(parameter, this);
        this.value = super.value;
        this.unit = super.unit;
    }

    /**
     * Returns the value of the Parameter as an Object.
     * 
     * @return An Object holding the value. The class type can of cause be found
     *         using reflection, or through the 'clazz' attribute.
     */
    public Number getValue() {
        return value;
    }

    public void setDoubleValue(double value) {
        this.value = value;
    }

    public double asDouble() {
        return value.doubleValue();
    }

    public int asInt() {
        return value.intValue();
    }

    /**
     * Setter for the value. Will also set the 'clazz' attribute to the Java
     * name of the class set as value.
     * 
     * @param value
     *        The value to be set.
     */
    public void setValue(Number value) {
        this.value = value;
    }

    /**
     * Returns the string representing the unit of this parameter.
     * 
     * @return String representing the unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit
     *        the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
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
        return super.value;
    }

    public void setRawDoubleValue(double value) {
        super.value = value;
    }

    public double rawAsDouble() {
        return super.value.doubleValue();
    }

    public int rawAsInt() {
        return super.value.intValue();
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
        super.value = value;
    }

    /**
     * Returns the string representing the unit of the raw parameter.
     * 
     * @return String representing the unit.
     */
    public String getRawUnit() {
        return super.unit;
    }

    /**
     * @param unit
     *        the unit to set
     */
    public void setRawUnit(String unit) {
        super.unit = unit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * 
     * Notice that the method has extra been implemented to allow comparison of
     * types that are not the same, i.e. Integer and Double.
     */
    @Override
    public int compareTo(EntityInstance cTo) {

        if (cTo instanceof Parameter) {
            Parameter rhs = (Parameter) cTo;
            if (this.value instanceof Short && rhs.value instanceof Short) {
                return ((Short) this.value).compareTo((Short) rhs.value);
            } else if (this.value instanceof Long && rhs.value instanceof Long) {
                return ((Long) this.value).compareTo((Long) rhs.value);
            } else if (this.value instanceof Integer && rhs.value instanceof Integer) {
                return ((Integer) this.value).compareTo((Integer) rhs.value);
            } else if (this.value instanceof Float && rhs.value instanceof Float) {
                return ((Float) this.value).compareTo((Float) rhs.value);
            } else if (this.value instanceof Double && rhs.value instanceof Double) {
                return ((Double) this.value).compareTo((Double) rhs.value);
            } else if (this.value instanceof Byte && rhs.value instanceof Byte) {
                return ((Byte) this.value).compareTo((Byte) rhs.value);
            } else if (this.value instanceof BigInteger && rhs.value instanceof BigInteger) {
                return ((BigInteger) this.value).compareTo((BigInteger) rhs.value);
            } else if (this.value instanceof BigDecimal && rhs.value instanceof BigDecimal) {
                return ((BigDecimal) this.value).compareTo((BigDecimal) rhs.value);
            } else {
                String message = String.format("Failed to compare values of %s and %s; value types are %s and %s",
                        this.toString(), rhs.toString(), this.value != null ? this.value.getClass() : "null",
                        rhs.value != null ? rhs.value.getClass() : "null");
                throw new RuntimeException(message);
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", name);
        builder.append("value", value);
        builder.append("rawvalue", super.value);
        builder.append("issuedBy", issuedBy);
        builder.append("applicableTo", applicableTo);
        return builder.build();
    }

}
