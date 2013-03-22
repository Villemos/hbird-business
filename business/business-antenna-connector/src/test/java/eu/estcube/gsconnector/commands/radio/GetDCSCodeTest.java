package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetDCSCodeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetDCSCode createMessage = new GetDCSCode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_DCS_CODE);
        string.append("+d\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
