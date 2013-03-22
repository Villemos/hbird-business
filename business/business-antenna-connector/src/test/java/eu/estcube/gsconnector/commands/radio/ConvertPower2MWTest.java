package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class ConvertPower2MWTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private ConvertPower2MW createMessage = new ConvertPower2MW();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.CONVERT_POWER2MW);
        command.addParameter(new TelemetryParameter("Power", 0.0));
        command.addParameter(new TelemetryParameter("Frequency", 0.0));
        command.addParameter(new TelemetryParameter("Mode", ".."));
        string.append("+2 0.0 0.0 ..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
