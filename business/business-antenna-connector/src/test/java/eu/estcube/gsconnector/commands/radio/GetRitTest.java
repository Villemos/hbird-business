package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetRitTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetRit createMessage = new GetRit();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_RIT);
        string.append("+j\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
