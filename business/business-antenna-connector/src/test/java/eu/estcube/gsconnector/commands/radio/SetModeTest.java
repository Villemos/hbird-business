package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetModeTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetMode createMessage = new SetMode();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_MODE);
        command.addParameter(new TelemetryParameter("Mode", "FM"));
        command.addParameter(new TelemetryParameter("Passband", 0));
        string.append("+M FM 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
