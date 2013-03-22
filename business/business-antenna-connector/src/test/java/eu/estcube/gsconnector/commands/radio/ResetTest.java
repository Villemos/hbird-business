package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class ResetTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Reset createMessage = new Reset();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.RESET);
        command.addParameter(new TelemetryParameter("Reset", 1));
        string.append("+* 1\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
