package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class VfoOperationTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private VfoOperation createMessage = new VfoOperation();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.VFO_OPERATION);
        command.addParameter(new TelemetryParameter("Mem/VFO Op", "CPY"));
        string.append("+G CPY\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
