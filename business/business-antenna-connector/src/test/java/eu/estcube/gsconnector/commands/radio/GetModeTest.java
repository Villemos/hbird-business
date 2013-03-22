package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetModeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetMode createMessage = new GetMode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_MODE);
        string.append("+m\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
