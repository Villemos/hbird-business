package eu.estcube.gsconnector.commands.radio;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eu.estcube.domain.TelemetryCommand;
import eu.estcube.domain.TelemetryParameter;
import eu.estcube.domain.TelemetryRadioConstants;

public class SetTuningStepTest {

    private StringBuilder string = new StringBuilder();
    private TelemetryCommand command;
    private SetTuningStep createMessage = new SetTuningStep();
    
    @Test
    public void testCreateMessageString() {
        command = new TelemetryCommand(TelemetryRadioConstants.SET_TUNING_STEP);
        command.addParameter(new TelemetryParameter("Tuning Step", 0));
        string.append("+N 0\n");
        assertEquals(string.toString(), createMessage.createMessageString(command).toString());
    }

}
