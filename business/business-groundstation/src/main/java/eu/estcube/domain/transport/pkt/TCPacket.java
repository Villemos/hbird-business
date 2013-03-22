package eu.estcube.domain.transport.pkt;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the data of ESTCube-1 telecommand packet.
 * 
 * @author sisaska
 * 
 */
public class TCPacket {
    private TCPacketPrimaryFrame primaryFrame;
    private List<TCPacketSecondaryFrame> secondaryFrames;

    public TCPacket() {
        super();
        secondaryFrames = new ArrayList<TCPacketSecondaryFrame>();
    }

    public TCPacket(TCPacketPrimaryFrame primaryFrame) {
        this();
        this.primaryFrame = primaryFrame;
    }

    public TCPacketPrimaryFrame getPrimaryFrame() {
        return primaryFrame;
    }

    public void setPrimaryFrame(TCPacketPrimaryFrame primaryFrame) {
        this.primaryFrame = primaryFrame;
    }

    public List<TCPacketSecondaryFrame> getSecondaryFrames() {
        return secondaryFrames;
    }

    public void addSecondaryFrame(TCPacketSecondaryFrame secondaryFrame) {
        secondaryFrames.add(secondaryFrame);
    }

}
