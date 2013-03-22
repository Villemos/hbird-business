package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetMemoryChannelNrTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetMemoryChannelNr createMessage = new GetMemoryChannelNr();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_MEMORY_CHANNELNR);
        string.append("+e\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
