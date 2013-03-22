package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class ScanTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Scan createMessage = new Scan();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SCAN);
        command.addParameter(new TelemetryParameter("Scan Fct", "STOP"));
        command.addParameter(new TelemetryParameter("Scan Channel", "DELTA"));
        string.append("+g STOP DELTA\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
