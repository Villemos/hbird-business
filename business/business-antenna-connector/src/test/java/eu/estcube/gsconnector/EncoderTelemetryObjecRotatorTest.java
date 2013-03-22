package eu.estcube.gsconnector;


import static org.junit.Assert.assertEquals;

import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRotatorConstants;

public class EncoderTelemetryObjecRotatorTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private EncoderTelemetryObjecRotator encode = new EncoderTelemetryObjecRotator();

    @Test
    public void testEncode() throws Exception
    {
        command = null;
        Object test = encode.encode(null, null, command);
        assertEquals(ChannelBuffers.EMPTY_BUFFER, test);

        command = new TelemetryCommand(TelemetryRotatorConstants.STOP);
        string.append("+S\n");
        assertEquals(string.toString(), encode.encode(null, null, command).toString());
    }

}
