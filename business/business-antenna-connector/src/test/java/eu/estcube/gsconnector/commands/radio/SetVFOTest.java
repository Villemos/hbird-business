package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetVFOTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetVFO createMessage = new SetVFO();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_VFO);
        command.addParameter(new TelemetryParameter("VFO", "VFO"));
        string.append("+V VFO\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
