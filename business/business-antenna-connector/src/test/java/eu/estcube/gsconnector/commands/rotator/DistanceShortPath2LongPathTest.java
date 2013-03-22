package eu.estcube.gsconnector.commands.rotator;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class DistanceShortPath2LongPathTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private DistanceShortPath2LongPath createMessage = new DistanceShortPath2LongPath();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.DISTANCE_SHORTPATH2LONGPATH);
        command.addParameter(new TelemetryParameter("Short Path km", 0.0));
        string.append("+a 0.0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
        
        string = new StringBuilder();
        command = new TelemetryCommand(TelemetryRotatorConstants.DISTANCE_SHORTPATH2LONGPATH);
        command.addParameter(new TelemetryParameter("Short Path km", null));
        string.append("+a null\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
        
    }

}
