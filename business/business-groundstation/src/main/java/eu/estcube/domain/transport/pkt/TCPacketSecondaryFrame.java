package eu.estcube.domain.transport.pkt;

/**
 * Encapsulates the data of primary frame of a packet.
 */
public class TCPacketSecondaryFrame extends TCPacketFrame {
    private TCPacketFrameSecondaryHeader header;

    public TCPacketSecondaryFrame() {
        super();
    }

    public TCPacketSecondaryFrame(TCPacketFrameSecondaryHeader header, byte[] body) {
        super(body);
        this.header = header;
    }

    public TCPacketFrameSecondaryHeader getHeader() {
        return header;
    }

    public void setHeader(TCPacketFrameSecondaryHeader header) {
        this.header = header;
    }

}
