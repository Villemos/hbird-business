package eu.estcube.gsconnector.commands;

import org.hbird.exchange.core.Command;

public class SendRawCmd implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        return messageString.append("+w ").append(command.getArgumentValue("Cmd", String.class)).append("\n");
    }
}
