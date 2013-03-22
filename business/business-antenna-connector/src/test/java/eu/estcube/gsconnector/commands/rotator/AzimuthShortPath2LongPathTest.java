package eu.estcube.gsconnector.commands.rotator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRotatorConstants;

public class AzimuthShortPath2LongPathTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private AzimuthShortPath2LongPath createMessage = new AzimuthShortPath2LongPath();

    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRotatorConstants.AZIMUTH_SHORTPATH2LONGPATH);
        command.addParameter(new TelemetryParameter("Short Path Deg", 0.0));
        string.append("+A 0.0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
