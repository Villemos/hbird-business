package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetRitTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetRIT createMessage = new SetRIT();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_RIT);
        command.addParameter(new TelemetryParameter("RIT", 0));
        string.append("+J 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
