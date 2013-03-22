package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetSplitVFOTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetSplitVFO createMessage = new GetSplitVFO();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_SPLIT_VFO);
        string.append("+s\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
