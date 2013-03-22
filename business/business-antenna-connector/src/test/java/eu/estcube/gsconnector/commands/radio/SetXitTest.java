package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetXitTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetXit createMessage = new SetXit();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_XIT);
        command.addParameter(new TelemetryParameter("XIT", 0));
        string.append("+Z 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
