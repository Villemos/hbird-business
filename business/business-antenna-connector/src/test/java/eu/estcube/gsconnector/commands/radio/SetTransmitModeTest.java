package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetTransmitModeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetTransmitMode createMessage = new SetTransmitMode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_TRANSMIT_MODE);
        command.addParameter(new TelemetryParameter("TX Mode", "AM"));
        command.addParameter(new TelemetryParameter("TX Passband", 0));
        string.append("+X AM 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
