package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetRptrOffsetTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetRptrOffset createMessage = new GetRptrOffset();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_RPTR_OFFSET);
        string.append("+o\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
