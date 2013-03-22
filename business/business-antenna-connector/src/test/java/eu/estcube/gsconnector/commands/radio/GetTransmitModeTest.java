package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetTransmitModeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetTransmitMode createMessage = new GetTransmitMode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_TRANSMIT_MODE);
        string.append("+x\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
