package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetTranceiveModeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetTranceiveMode createMessage = new GetTranceiveMode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_TRANCEIVE_MODE);
        string.append("+a\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
