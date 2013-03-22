package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetXitTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetXit createMessage = new GetXit();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_XIT);
        string.append("+z\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
