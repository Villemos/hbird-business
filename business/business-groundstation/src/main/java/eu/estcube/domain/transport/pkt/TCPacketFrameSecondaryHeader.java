package eu.estcube.domain.transport.pkt;

/**
 * Encapsulates the fields of ESTCube-1 TC packet seconder header (header of the
 * frames following the first frame).
 * See details http://tudengisatelliit.ut.ee:8080/display/MCS/TC+Packet+Encoder
 */
public class TCPacketFrameSecondaryHeader extends TCPacketFrameHeader {
    /* No specific fields comparing to the parent class */

    public TCPacketFrameSecondaryHeader() {
        super();
    }

    public TCPacketFrameSecondaryHeader(int ssc, int len) {
        super(ssc, len);
    }

}
