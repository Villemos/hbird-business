package eu.estcube.domain.transport.pkt;

/**
 * Encapsulates the common fields of primary and secondary packet headers,
 * see details on
 * http://tudengisatelliit.ut.ee:8080/display/MCS/TC+Packet+Encoder
 */
public class TCPacketFrameHeader {
    public static final int SSC_MAX = 4194303;
    public static final byte SSC_BYTES = 3;

    public static final int LEN_MAX = 65535;
    public static final byte LEN_BYTES = 2;

    /* Source sequence counter, 22 bits unsigned integer, 0 - 4,194,303 */
    private int ssc;

    /* Length of packet in AX.25 frames, 2 bytes unsigned integer, 0 - 65,535 */
    private int len;

    public TCPacketFrameHeader() {
    }

    public TCPacketFrameHeader(int ssc, int len) {
        super();
        this.ssc = ssc;
        this.len = len;
    }

    public int getSsc() {
        return ssc;
    }

    public void setSsc(int ssc) {
        this.ssc = ssc;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
