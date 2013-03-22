package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetLevelTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetLevel createMessage = new GetLevel();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_LEVEL);
        command.addParameter(new TelemetryParameter("Level", "ATT"));
        string.append("+l ATT\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
