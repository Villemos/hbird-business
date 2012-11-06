package org.hbird.business.simpleparametersimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class TestCommandSender {

	public CommandRequest send() {
		Map<String, Object> arguments = new HashMap<String, Object>();		
		
		Command command = new Command("Simulator", "ESTcube/platform/battery1", "Turn Off", "Command to turn of the battery. NOTE: SIMULATED", 0, 0, arguments);
		
		List<String> lockstates = new ArrayList<String>();
		lockstates.add("TestParameter3LowerLimit");
		
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new SetParameter("", "TestTask", "Task for testing", 0, new Parameter("", "Parameter90", "", "Parameter set by task", 9d, "Volt")));
		
		return new CommandRequest("Simulator", "TestCommandContainer", "A container for the test command used for validation", lockstates, tasks, command);
	}	
}
