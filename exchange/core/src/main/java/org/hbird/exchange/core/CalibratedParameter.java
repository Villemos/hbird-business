package org.hbird.exchange.core;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

/**
 * A CALIBRATED NUMERICAL parameter. Extends <code>Parameter</code>, holds
 * calibrated value, its formatting pattern and level.
 * 
 */
public class CalibratedParameter extends Parameter {

    private static final long serialVersionUID = 5868880355306107374L;

    /** Calibrated value of the parameter. */
    protected Number calibratedValue;

    /** Format pattern to apply to the calibrated value */
    protected String formatPattern;

    /** Parameter limit level data */
    protected Level level;

    /** Parameter limit level states */
    public enum Level {
        /**
         * The value hasn't been checked
         */
        NOT_CHECKED(0),
        /**
         * The value is in all of the limits (green zone/OK)
         */
        SOFT(1),
        /**
         * The value isn't in the soft limits (yellow zone/warning)
         */
        HARD(2),
        /**
         * The value isn't in the hard limits (red zone/error)
         */
        SANE(3),
        /**
         * The value is out of all of the limits (gray zone/error)
         */
        OUT(4);
        Level(int value) {
            this.value = value;
        }

        public int value;
    }

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

        // A fix to superclass's value not being set, because setValue method is
        // used by BeanUtils.copyProperties
        if (calibratedValue != null)
            super.value = calibratedValue;
    }

    /**
     * Returns the value of the Parameter as an Object.
     * 
     * @return An Object holding the value. The class type can of cause be found
     *         using reflection, or through the 'clazz' attribute.
     */
    @Override
    public Number getValue() {
        return calibratedValue;
    }

    @Override
    public void setDoubleValue(double value) {
        calibratedValue = value;
    }

    @Override
    public double asDouble() {
        return calibratedValue.doubleValue();
    }

    @Override
    public int asInt() {
        return calibratedValue.intValue();
    }

    /**
     * Setter for the value. Will also set the 'clazz' attribute to the Java
     * name of the class set as value.
     * 
     * @param value
     *        The value to be set.
     */
    @Override
    public void setValue(Number value) {
        calibratedValue = value;
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
        return super.getValue();
    }

    public void setRawDoubleValue(double value) {
        super.setDoubleValue(value);
    }

    public double rawAsDouble() {
        return super.asDouble();
    }

    public int rawAsInt() {
        return super.asInt();
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
        super.setValue(value);
    }

    /**
     * 
     * @return a <code>String</code> containing format pattern which can be
     *         applied to calibrated value
     */
    public String getFormatPattern() {
        return formatPattern;
    }

    /**
     * 
     * @param formatPattern
     *        New formatPattern to set
     */
    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Level level) {
        this.level = level;
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
            if (this.calibratedValue instanceof Short && rhs.value instanceof Short) {
                return ((Short) this.calibratedValue).compareTo((Short) rhs.value);
            } else if (this.calibratedValue instanceof Long && rhs.value instanceof Long) {
                return ((Long) this.calibratedValue).compareTo((Long) rhs.value);
            } else if (this.calibratedValue instanceof Integer && rhs.value instanceof Integer) {
                return ((Integer) this.calibratedValue).compareTo((Integer) rhs.value);
            } else if (this.calibratedValue instanceof Float && rhs.value instanceof Float) {
                return ((Float) this.calibratedValue).compareTo((Float) rhs.value);
            } else if (this.calibratedValue instanceof Double && rhs.value instanceof Double) {
                return ((Double) this.calibratedValue).compareTo((Double) rhs.value);
            } else if (this.calibratedValue instanceof Byte && rhs.value instanceof Byte) {
                return ((Byte) this.calibratedValue).compareTo((Byte) rhs.value);
            } else if (this.calibratedValue instanceof BigInteger && rhs.value instanceof BigInteger) {
                return ((BigInteger) this.calibratedValue).compareTo((BigInteger) rhs.value);
            } else if (this.calibratedValue instanceof BigDecimal && rhs.value instanceof BigDecimal) {
                return ((BigDecimal) this.calibratedValue).compareTo((BigDecimal) rhs.value);
            } else {
                String message = String.format("Failed to compare values of %s and %s; value types are %s and %s",
                        this.toString(), rhs.toString(), this.calibratedValue != null ? this.calibratedValue.getClass()
                                : "null",
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
        builder.append("calibratedvalue", calibratedValue);
        builder.append("rawvalue", value);
        builder.append("issuedBy", issuedBy);
        builder.append("applicableTo", applicableTo);
        return builder.build();
    }

}
