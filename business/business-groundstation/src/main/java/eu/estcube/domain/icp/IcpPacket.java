package eu.estcube.domain.icp;

public class IcpPacket {

    private byte destincation;

    private byte source;

    private byte priority;

    private byte index;

    private byte[] payload;

    public byte getIndex() {
        return index;
    }

    public void setIndex(byte index) {
        this.index = index;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public byte getDestincation() {
        return destincation;
    }

    public void setDestincation(byte destincation) {
        this.destincation = destincation;
    }

    public byte getSource() {
        return source;
    }

    public void setSource(byte source) {
        this.source = source;
    }

}
