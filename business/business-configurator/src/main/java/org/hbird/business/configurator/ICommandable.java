package org.hbird.business.configurator;

import org.hbird.exchange.core.Command;

public interface ICommandable {

	public void receiveCommand(Command command);
}
