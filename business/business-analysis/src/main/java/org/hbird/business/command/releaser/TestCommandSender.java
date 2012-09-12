package org.hbird.business.command.releaser;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class TestCommandSender {

	public CommandRequest send() {
		
		// String issuedBy, String name, String description, long releaseTime, long executionTime, List<String> lockStates, List<Task> tasks, Command command
		
		List<Parameter> arguments = new ArrayList<Parameter>();		
		
		Command command = new Command("Simulator", "TestCommand", "A command used for testing", 0, 0, arguments);
		
		List<String> lockstates = new ArrayList<String>();
		lockstates.add("TestParameter3LowerLimit");
		
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new SetParameter("TestTask", "Task for testing", 0, new Parameter("", "Parameter90", "Parameter set by task", 9d, "Volt")));
		
		return new CommandRequest("Simulator", "TestCommandContainer", "A container for the test command used for validation", lockstates, tasks, command);
	}
	
}
