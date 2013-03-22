package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetPttTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetPTT createMessage = new SetPTT();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_PTT);
        command.addParameter(new TelemetryParameter("PTT", 0));
        string.append("+T 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
