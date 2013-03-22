package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryRadioConstants;

public class GetTuningStepTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private GetTuningStep createMessage = new GetTuningStep();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.GET_TUNING_STEP);
        string.append("+n\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
