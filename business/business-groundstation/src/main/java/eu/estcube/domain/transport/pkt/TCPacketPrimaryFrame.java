package eu.estcube.domain.transport.pkt;

/**
 * Encapsulates the data of primary frame of a packet.
 */
public class TCPacketPrimaryFrame extends TCPacketFrame {
    private TCPacketFramePrimaryHeader header;

    public TCPacketPrimaryFrame() {
        super();
    }

    public TCPacketPrimaryFrame(TCPacketFramePrimaryHeader header, byte[] body) {
        super(body);
        this.header = header;
    }

    public TCPacketFramePrimaryHeader getHeader() {
        return header;
    }

    public void setHeader(TCPacketFramePrimaryHeader header) {
        this.header = header;
    }

}
