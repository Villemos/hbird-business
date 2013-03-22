package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetRptrShiftTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetRptrShift createMessage = new GetRptrShift();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_RPTR_SHIFT);
        string.append("+r\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
