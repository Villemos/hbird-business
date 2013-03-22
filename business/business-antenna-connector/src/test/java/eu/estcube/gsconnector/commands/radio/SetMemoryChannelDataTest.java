package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetMemoryChannelDataTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetMemoryChannelData createMessage = new SetMemoryChannelData();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_MEMORY_CHANNELDATA);
        command.addParameter(new TelemetryParameter("Channel", ".."));
        string.append("+H ..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
