package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetMemoryChannelNrTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetMemoryChannelNr createMessage = new SetMemoryChannelNr();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_MEMORY_CHANNELNR);
        command.addParameter(new TelemetryParameter("Memory#", 0));
        string.append("+E 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
