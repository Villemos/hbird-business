package eu.estcube.domain.transport.ax25;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Encapsulates the data of a AX.25 frame. Please note that all byte / bit order
 * in this class is as per Java. It does not take into account that, for
 * instance, in actual transmission of the data LSB bit order is used. This will
 * be handled when decoding / encoding the frame data.
 */
public class Ax25UIFrame implements Serializable {

    private static final long serialVersionUID = -4166760891547388393L;

    public static final byte FLAG = (byte) 0x7E;
    public static final byte CTRL = (byte) 0x03;
    public static final byte PID = (byte) 0xF0;

    public static final int DEST_ADDR_LEN = 7; // bytes
    public static final int SRC_ADDR_LEN = 7; // bytes
    public static final int FCS_LEN = 2; // bytes

    /**
     * ADDR + CTRL + PID + FCS: 14 + 1 + 1 + 2
     */
    public static final int NON_INFO_LENGTH = 18;
    public static final int INFO_MAX_SIZE = 256;
    public static final int FRAME_CONTENTS_MAX_SIZE = INFO_MAX_SIZE + NON_INFO_LENGTH;

    // public static final String UNSTUFFED_ERROR_MSG =
    // "Frame contains a sequence of 7 unstuffed bits";
    // public static final String MAX_FRAME_LENGTH_ERROR_MSG =
    // "Frame length limit exceeded. Maximum allowed is "
    // + FRAME_CONTENTS_MAX_SIZE + " bytes";
    // public static final String MIN_FRAME_LENGTH_ERROR_MSG =
    // "Frame length is less than " + NON_INFO_LENGTH + " bytes";
    // public static final String UNALIGNED_ERROR_MSG =
    // "Number of bits in frame is not octet aligned";
    // public static final String FCS_ERROR_MSG =
    // "FCS error: frame crc: %s, computed crc: %s";

    private byte[] destAddr;
    private byte[] srcAddr;
    private byte ctrl;
    private byte pid;
    private byte[] info;
    private byte[] fcs;
    // private Set<String> errorMsgs; // processing errors

    private Ax25FrameStatus status = new Ax25FrameStatus(false, false, false, false, false);

    public Ax25UIFrame() {
    }

    public Ax25UIFrame(byte[] destAddr, byte[] srcAddr, byte[] info, byte[] fcs) {
        this(destAddr, srcAddr, CTRL, PID, info, fcs);
    }

    public Ax25UIFrame(byte[] destAddr, byte[] srcAddr, byte ctrl, byte pid, byte[] info, byte[] fcs) {
        super();
        this.destAddr = destAddr;
        this.srcAddr = srcAddr;
        this.ctrl = ctrl;
        this.pid = pid;
        this.info = info;
        this.fcs = fcs;
    }

    // public void addErrorMsg(String errorMsg) {
    // if (errorMsgs == null) {
    // errorMsgs = new HashSet<String>(5);
    // }
    // errorMsgs.add(errorMsg);
    // }

    // @SuppressWarnings("unchecked")
    // @Override
    // public void readExternal(ObjectInput in) throws IOException,
    // ClassNotFoundException {
    // destAddr = ByteUtil.read(in);
    // srcAddr = ByteUtil.read(in);
    // ctrl = in.readByte();
    // pid = in.readByte();
    // info = ByteUtil.read(in);
    // fcs = ByteUtil.read(in);
    // errorMsgs = (Set<String>) in.readObject();
    // in.close();
    // }
    //
    // @Override
    // public void writeExternal(ObjectOutput out) throws IOException {
    // ByteUtil.write(destAddr, out);
    // ByteUtil.write(srcAddr, out);
    // out.writeByte(ctrl);
    // out.writeByte(pid);
    // ByteUtil.write(info, out);
    // ByteUtil.write(fcs, out);
    // out.writeObject(errorMsgs);
    // out.flush();
    // }

    // public String toString() {
    // StringBuilder sb = new StringBuilder();
    // sb.append("Ax25Frame:\n");
    // sb.append("dest: ").append(ByteUtil.hex(destAddr)).append("\n");
    // sb.append("src:  ").append(ByteUtil.hex(srcAddr)).append("\n");
    // sb.append("ctrl: ").append(ByteUtil.hex(ctrl)).append("\n");
    // sb.append("pid:  ").append(ByteUtil.hex(pid)).append("\n");
    // sb.append("info: ");
    // if (info != null) {
    // sb.append("(len=").append(info.length).append(") ");
    // }
    // sb.append(ByteUtil.hex(info)).append("\n");
    // sb.append("fcs:  ").append(ByteUtil.hex(fcs)).append("\n");
    // sb.append("errs: ").append(errorMsgs).append("\n");
    // return sb.toString();
    // }

    // -------------- The rest is generated ------------------

    public Ax25FrameStatus getStatus() {
        return status;
    }

    public void setStatus(Ax25FrameStatus status) {
        this.status = status;
    }

    public byte[] getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(byte[] destAddr) {
        this.destAddr = destAddr;
    }

    public byte[] getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(byte[] srcAddr) {
        this.srcAddr = srcAddr;
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public byte[] getFcs() {
        return fcs;
    }

    public void setFcs(byte[] fcs) {
        this.fcs = fcs;
    }

    public byte getCtrl() {
        return ctrl;
    }

    public void setCtrl(byte ctrl) {
        this.ctrl = ctrl;
    }

    public byte getPid() {
        return pid;
    }

    public void setPid(byte pid) {
        this.pid = pid;
    }

    // public Set<String> getErrorMsgs() {
    // return errorMsgs;
    // }
    //
    // public void setErrorMsgs(Set<String> errorMsgs) {
    // this.errorMsgs = errorMsgs;
    // }
    //
    // public boolean hasErrors() {
    // return errorMsgs != null && !errorMsgs.isEmpty();
    // }

    public boolean isErrorUnstuffedBits() {
        return status.isErrorUnstuffedBits();
    }

    public void setErrorUnstuffedBits(boolean errorUnstuffedBits) {
        this.status.setErrorUnstuffedBits(errorUnstuffedBits);
    }

    public boolean isErrorTooLong() {
        return status.isErrorTooLong();
    }

    public void setErrorTooLong(boolean errorTooLong) {
        this.status.setErrorTooLong(errorTooLong);
    }

    public boolean isErrorTooShort() {
        return status.isErrorTooShort();
    }

    public void setErrorTooShort(boolean errorTooShort) {
        this.status.setErrorTooShort(errorTooShort);
    }

    public boolean isErrorUnAligned() {
        return status.isErrorUnAligned();
    }

    public void setErrorUnAligned(boolean errorUnAligned) {
        this.status.setErrorUnAligned(errorUnAligned);
    }

    public boolean isErrorFcs() {
        return status.isErrorFcs();
    }

    public void setErrorFcs(boolean errorFcs) {
        this.status.setErrorFcs(errorFcs);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ctrl;
        result = prime * result + Arrays.hashCode(destAddr);
        result = prime * result + Arrays.hashCode(fcs);
        result = prime * result + Arrays.hashCode(info);
        result = prime * result + pid;
        result = prime * result + Arrays.hashCode(srcAddr);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ax25UIFrame other = (Ax25UIFrame) obj;
        if (ctrl != other.ctrl)
            return false;
        if (!Arrays.equals(destAddr, other.destAddr))
            return false;
        if (!Arrays.equals(fcs, other.fcs))
            return false;
        if (!Arrays.equals(info, other.info))
            return false;
        if (pid != other.pid)
            return false;
        if (!Arrays.equals(srcAddr, other.srcAddr))
            return false;
        return true;
    }
}
