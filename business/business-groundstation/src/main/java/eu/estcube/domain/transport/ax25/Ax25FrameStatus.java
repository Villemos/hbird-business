package eu.estcube.domain.transport.ax25;

import java.io.Serializable;

public class Ax25FrameStatus implements Serializable {

    private static final long serialVersionUID = 2679992282117256969L;

    private boolean errorUnstuffedBits;
    private boolean errorTooLong;
    private boolean errorTooShort;
    private boolean errorUnAligned;
    private boolean errorFcs;

    public Ax25FrameStatus() {
        this(false, false, false, false, false);
    }

    public Ax25FrameStatus(boolean errorUnstuffedBits, boolean errorTooLong, boolean errorTooShort,
            boolean errorUnAligned, boolean errorFcs) {
        this.errorUnstuffedBits = errorUnstuffedBits;
        this.errorTooLong = errorTooLong;
        this.errorTooShort = errorTooShort;
        this.errorUnAligned = errorUnAligned;
        this.errorFcs = errorFcs;
    }

    public void setValid() {
        this.errorUnstuffedBits = false;
        this.errorTooLong = false;
        this.errorTooShort = false;
        this.errorUnAligned = false;
        this.errorFcs = false;
    }

    public boolean isValid() {
        return !(isErrorFcs() || isErrorTooLong() || isErrorTooShort()
                || isErrorUnAligned() || isErrorUnstuffedBits());
    }

    public boolean isErrorUnstuffedBits() {
        return errorUnstuffedBits;
    }

    public void setErrorUnstuffedBits(boolean errorUnstuffedBits) {
        this.errorUnstuffedBits = errorUnstuffedBits;
    }

    public boolean isErrorTooLong() {
        return errorTooLong;
    }

    public void setErrorTooLong(boolean errorTooLong) {
        this.errorTooLong = errorTooLong;
    }

    public boolean isErrorTooShort() {
        return errorTooShort;
    }

    public void setErrorTooShort(boolean errorTooShort) {
        this.errorTooShort = errorTooShort;
    }

    public boolean isErrorUnAligned() {
        return errorUnAligned;
    }

    public void setErrorUnAligned(boolean errorUnAligned) {
        this.errorUnAligned = errorUnAligned;
    }

    public boolean isErrorFcs() {
        return errorFcs;
    }

    public void setErrorFcs(boolean errorFcs) {
        this.errorFcs = errorFcs;
    }
}