package eu.estcube.gsconnector;


import static org.junit.Assert.assertEquals;

import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;
import eu.estcube.domain.TelemetryRotatorConstants;

public class EncoderTelemetryObjectAbstractTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private EncoderTelemetryObjectAbstract encode;

    @Test
    public void testRadioEncode() throws Exception
    {
        encode = new EncoderTelemetryObjecRadio();
        command = null;
        Object test = encode.encode(null, null, command);
        assertEquals(ChannelBuffers.EMPTY_BUFFER, test);

        command = new TelemetryCommand(TelemetryRadioConstants.GET_MODE);
        string.append("+m\n");
        assertEquals(string.toString(), encode.encode(null, null, command).toString());
    }
    
    @Test
    public void testRotatorEncode() throws Exception
    {
        encode = new EncoderTelemetryObjecRotator();
        command = null;
        Object test = encode.encode(null, null, command);
        assertEquals(ChannelBuffers.EMPTY_BUFFER, test);

        command = new TelemetryCommand(TelemetryRotatorConstants.STOP);
        string.append("+S\n");
        assertEquals(string.toString(), encode.encode(null, null, command).toString());
    }

}
