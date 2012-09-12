package org.hbird.business.command.releaser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.StateParameter;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;
import org.junit.Test;

public class CommandReleaserTest {

	@Test
	public void testProcess() {
		List<Parameter> arguments = new ArrayList<Parameter>();		
		
		Command command = new Command("Simulator", "TestCommand", "A command used for testing", 0, 0, arguments);
		
		List<String> lockstates = new ArrayList<String>();
		lockstates.add("TestParameter3LowerLimit");
		lockstates.add("TestParameter4LowerLimit");
		
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(new SetParameter("TestTask", "Task for testing", 0, new Parameter("", "Parameter90", "Parameter set by task", 9d, "Volt")));
			
		Map<String, Object> headers = new HashMap<String, Object>();
		
		CommandReleaser releaser = new CommandReleaser();
		
		CommandRequest request = new CommandRequest("Simulator", "TestCommandContainer", "A container for the test command used for validation", lockstates, tasks, null);		
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == false);
		
		request = new CommandRequest("Simulator", "TestCommandContainer", "A container for the test command used for validation", lockstates, tasks, command);		
		headers.clear();
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == false);
		
		/** Add a few stages. */
		releaser.state(new StateParameter("", "TestState1", "A test state", "Something", false));
		releaser.state(new StateParameter("", "TestState2", "A test state", "Something", true));
		releaser.state(new StateParameter("", "TestState3", "A test state", "Something", false));

		/** We will still fail, as the proper state has not been set. */
		headers.clear();
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == false);

		/** Add one of the states. */
		releaser.state(new StateParameter("", "TestParameter3LowerLimit", "A test state", "Something", false));

		/** We will still fail, as the proper state has been set wrong. */
		headers.clear();
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == false);

		/** Add one of the states. */
		releaser.state(new StateParameter("", "TestParameter4LowerLimit", "A test state", "Something", false));

		/** We will still fail, as the proper state has been set wrong. */
		headers.clear();
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == false);

		/** Add one of the states. */
		releaser.state(new StateParameter("", "TestParameter3LowerLimit", "A test state", "Something", true));

		/** We will still fail, as the proper state has been set wrong. */
		headers.clear();
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == false);

		/** Add one of the states. */
		releaser.state(new StateParameter("", "TestParameter4LowerLimit", "A test state", "Something", true));

		/** We will still fail, as the proper state has been set wrong. */
		headers.clear();
		releaser.process(request, headers);
		assertTrue((Boolean) headers.get("Valid") == true);

	}
}
