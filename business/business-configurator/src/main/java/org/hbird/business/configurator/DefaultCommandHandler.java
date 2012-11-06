package org.hbird.business.configurator;

import org.hbird.exchange.core.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCommandHandler implements ICommandable {

	private static final transient Logger LOG = LoggerFactory.getLogger(ComponentBuilder.class);
	
	public void receiveCommand(Command command) {
		LOG.error("Received command. This type of component does not provide a commandable interface. Command discarded.");
	}
}
