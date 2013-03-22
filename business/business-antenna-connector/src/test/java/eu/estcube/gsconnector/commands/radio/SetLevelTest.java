package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetLevelTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetLevel createMessage = new SetLevel();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_LEVEL);
        command.addParameter(new TelemetryParameter("Level", "ATT"));
        command.addParameter(new TelemetryParameter("Level Value", 0));
        string.append("+L ATT 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
