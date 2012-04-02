package org.hbird.exchange.commandrelease;

import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.ServiceSpecification;
import org.hbird.exchange.tasking.Task;

public class CommandReleaseServiceSpecification extends ServiceSpecification {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6404153302726506261L;

	{
		this.name = "Command Release Service";
		this.description = "The command release service takes a command request, schedules the command, performs the pre-release validation and schedule the tasks needed to reconfigure the system based on the affects that the command will have. It then releases the command.";

		this.logicalIn.put("commandrequest", new CommandRequest());
		
		this.logicalOut.put("tasks", new Task());
		this.logicalOut.put("command", new Command());	
	}
	
}
