package eu.estcube.gsconnector.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class CapabilitiesTest {
    
    
    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command = new TelemetryCommand(TelemetryRadioConstants.CAPABILITIES);
    private Capabilities createMessage = new Capabilities();
    

    @Test
    public void testCreateMessageString() {
        string.append("+1\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
