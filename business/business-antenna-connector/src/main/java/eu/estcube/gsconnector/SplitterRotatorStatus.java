package eu.estcube.gsconnector;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import eu.estcube.domain.TelemetryRotatorConstants;

@Component
public class SplitterRotatorStatus extends SplitterAbstractStatus {

    private List<String> commands;

    public SplitterRotatorStatus() {
        this.commands = new ArrayList<String>();
        commands.add(TelemetryRotatorConstants.GET_POSITION);
    }

    @Override
    protected List<String> getCommands() {
        return commands;
    }

}