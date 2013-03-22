package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetTransmitFrequencyTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetTransmitFrequency createMessage = new GetTransmitFrequency();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_TRANSMIT_FREQUENCY);
        string.append("+i\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
