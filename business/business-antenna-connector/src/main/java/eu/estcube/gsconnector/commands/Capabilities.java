package eu.estcube.gsconnector.commands;

import org.hbird.exchange.core.Command;

public class Capabilities implements HamlibCommandBuilder {

    public StringBuilder createMessageString(Command command) {
        StringBuilder messageString = new StringBuilder();
        return messageString.append("+1\n");
    }
}
