package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetRprtShiftTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetRprtShift createMessage = new SetRprtShift();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_RPTR_SHIFT);
        command.addParameter(new TelemetryParameter("Rptr Shift", "+"));
        string.append("+R +\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
