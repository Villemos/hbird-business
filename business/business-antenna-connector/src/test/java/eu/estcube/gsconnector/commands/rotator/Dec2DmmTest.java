package eu.estcube.gsconnector.commands.rotator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class Dec2DmmTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private Dec2Dmm createMessage = new Dec2Dmm();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.DEC2DMMM);
        command.addParameter(new TelemetryParameter("Dec Deg", 0.0));
        string.append("+e 0.0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
