package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetFrequencyTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetFrequency createMessage = new GetFrequency();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_FREQUENCY);
        string.append("+f\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
