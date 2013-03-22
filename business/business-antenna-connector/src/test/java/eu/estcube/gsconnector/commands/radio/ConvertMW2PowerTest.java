package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class ConvertMW2PowerTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private ConvertMW2Power createMessage = new ConvertMW2Power();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.CONVERT_MW2POWER);
        command.addParameter(new TelemetryParameter("Power mW", 0));
        command.addParameter(new TelemetryParameter("Frequency", 0));
        command.addParameter(new TelemetryParameter("Mode", ".."));
        string.append("+4 0 0 ..\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
