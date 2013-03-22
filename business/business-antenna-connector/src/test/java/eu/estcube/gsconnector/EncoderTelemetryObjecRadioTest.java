package eu.estcube.gsconnector;


import static org.junit.Assert.assertEquals;

import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class EncoderTelemetryObjecRadioTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private EncoderTelemetryObjecRadio encode = new EncoderTelemetryObjecRadio();

    @Test
    public void testEncode() throws Exception
    {
        command = null;
        Object test = encode.encode(null, null, command);
        assertEquals(ChannelBuffers.EMPTY_BUFFER, test);

        command = new TelemetryCommand(TelemetryRadioConstants.GET_MODE);
        string.append("+m\n");
        assertEquals(string.toString(), encode.encode(null, null, command).toString());
    }

}
