package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class QRBTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private QRB createMessage = new QRB();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.QRB);
        command.addParameter(new TelemetryParameter("Lon 1", 0.0));
        command.addParameter(new TelemetryParameter("Lat 1", 0.0));
        command.addParameter(new TelemetryParameter("Lon 2", 0.0));
        command.addParameter(new TelemetryParameter("Lat 2", 0.0));
        string.append("+B 0.0 0.0 0.0 0.0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
