package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class ConfigurationTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command = new TelemetryCommand(TelemetryRadioConstants.CONFIGURATION);
    private Configuration createMessage = new Configuration();
    
    @Test
    public void testCreateMessageString() {
        string.append("+3\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
