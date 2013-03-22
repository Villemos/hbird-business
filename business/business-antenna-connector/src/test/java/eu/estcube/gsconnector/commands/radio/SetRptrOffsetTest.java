package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetRptrOffsetTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetRptrOffset createMessage = new SetRptrOffset();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_RPTR_OFFSET);
        command.addParameter(new TelemetryParameter("Rptr Offset", 0));
        string.append("+O 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
