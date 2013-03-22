package eu.estcube.gsconnector;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import eu.estcube.domain.TelemetryRadioConstants;

@Component
public class SplitterRadioStatus extends SplitterAbstractStatus {

    private List<String> commands;

    public SplitterRadioStatus() {
        this.commands = new ArrayList<String>();
        commands.add(TelemetryRadioConstants.GET_ANTENNA_NR);
        commands.add(TelemetryRadioConstants.GET_CTCSS_TONE);
        commands.add(TelemetryRadioConstants.GET_DCS_CODE);
        commands.add(TelemetryRadioConstants.GET_FREQUENCY);
        commands.add(TelemetryRadioConstants.GET_MEMORY_CHANNELNR);
        commands.add(TelemetryRadioConstants.GET_MODE);
        commands.add(TelemetryRadioConstants.GET_PTT);
        commands.add(TelemetryRadioConstants.GET_RIT);
        commands.add(TelemetryRadioConstants.GET_RPTR_OFFSET);
        commands.add(TelemetryRadioConstants.GET_RPTR_SHIFT);
        commands.add(TelemetryRadioConstants.GET_SPLIT_VFO);
        commands.add(TelemetryRadioConstants.GET_TRANCEIVE_MODE);
        commands.add(TelemetryRadioConstants.GET_TRANSMIT_FREQUENCY);
        commands.add(TelemetryRadioConstants.GET_TRANSMIT_MODE);
        commands.add(TelemetryRadioConstants.GET_TUNING_STEP);
        commands.add(TelemetryRadioConstants.GET_VFO);
        commands.add(TelemetryRadioConstants.GET_XIT);
    }

    @Override
    protected List<String> getCommands() {
        return commands;
    }
}