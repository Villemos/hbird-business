package eu.estcube.gsconnector;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.springframework.stereotype.Component;

import eu.estcube.domain.TelemetryCommand;

@Component
public abstract class SplitterAbstractStatus {

    protected abstract List<String> getCommands();

    public List<Message> splitMessage() {
        List<Message> answer = new ArrayList<Message>();

        for (int i = 0; i < getCommands().size(); i++) {
            DefaultMessage message = new DefaultMessage();
            message.setBody(new TelemetryCommand(getCommands().get(i)));
            answer.add(message);
        }
        return answer;
    }
}