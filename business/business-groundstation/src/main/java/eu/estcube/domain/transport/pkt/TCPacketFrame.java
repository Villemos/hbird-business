package eu.estcube.domain.transport.pkt;

/**
 * Encapsulates the data of one frame of a packet.
 */
public class TCPacketFrame {
    private byte[] body;

    public TCPacketFrame() {
    }

    public TCPacketFrame(byte[] body) {
        super();
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
