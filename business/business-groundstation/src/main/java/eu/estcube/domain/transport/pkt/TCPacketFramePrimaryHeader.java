package eu.estcube.domain.transport.pkt;

/**
 * Encapsulates the fields of ESTCube-1 TC packet primary header (header of the
 * first frame).
 * See details http://tudengisatelliit.ut.ee:8080/display/MCS/TC+Packet+Encoder
 */
public class TCPacketFramePrimaryHeader extends TCPacketFrameHeader {
    // 2 bytes, unsigned
    public static final int SPID_MAX = 65535;
    public static final byte SPID_BYTES = 2;

    // 4 bytes unsigned
    public static final long TS_MAX = 4294967295L;
    public static final byte TS_BYTES = 4;

    // 4 bytes
    public static final byte MAC_BYTES = 4;

    // 5 bytes unsigned
    public static final long ET_MAX = 109951162777500L;
    public static final byte ET_BYTES = 5;

    /* Source packet ID, 2 bytes unsigned integer, 0 - 65,535 */
    private int spid;

    /*
     * Timestamp when packet was created.
     * 4 bytes unsigned integer, 0 - 4,294,967,295, seconds (not milliseconds)
     * since 01.01.1970 00:00 GMT
     */
    private long ts;

    /* Message authentication code, 4 bytes */
    private byte[] mac;

    /*
     * Execution time, 5 byte unsigned integer, 1/10 seconds since 01.01.1970
     * 00:00 GMT
     */
    private long et;

    public TCPacketFramePrimaryHeader() {
    }

    public TCPacketFramePrimaryHeader(int ssc, int len, int spid, long ts, byte[] mac, long et) {
        super(ssc, len);
        this.spid = spid;
        this.ts = ts;
        this.mac = mac;
        this.et = et;
    }

    public int getSpid() {
        return spid;
    }

    public void setSpid(int spid) {
        this.spid = spid;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public long getEt() {
        return et;
    }

    public void setEt(long et) {
        this.et = et;
    }
}
