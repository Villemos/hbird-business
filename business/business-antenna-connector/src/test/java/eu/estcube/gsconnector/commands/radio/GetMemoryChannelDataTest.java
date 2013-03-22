package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetMemoryChannelDataTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetMemoryChannelData createMessage = new GetMemoryChannelData();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_MEMORY_CHANNELDATA);
        command.addParameter(new TelemetryParameter("Channel", ".."));
        string.append("+h ..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
