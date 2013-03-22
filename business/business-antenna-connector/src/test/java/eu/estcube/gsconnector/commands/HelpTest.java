package eu.estcube.gsconnector.commands;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class HelpTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Help createMessage = new Help();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.HELP);
        string.append("??+_\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
