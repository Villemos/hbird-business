package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetPttTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetPTT createMessage = new GetPTT();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_PTT);
        string.append("+t\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
