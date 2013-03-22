package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetVFOTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetVFO createMessage = new GetVFO();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_VFO);
        string.append("+v\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
