package org.hbird.business.systemtest;

import org.apache.camel.Handler;
import org.hbird.exchange.configurator.StartTaskExecutorComponent;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.SendCommand;
import org.hbird.exchange.tasking.SetParameter;
import org.hbird.exchange.tasking.Task;

public class TaskingTester extends Tester {

	@Handler
	public void process() throws InterruptedException {
	
		/** Start two task executors. */
		injection.sendBody(new StartTaskExecutorComponent("TaskExecutor1"));
		injection.sendBody(new StartTaskExecutorComponent("TaskExecutor2"));
		
		Thread.sleep(2000);
		
		/** Create simple task that sets a parameter. Is executed immediatly. */
		injection.sendBody(new SetParameter("SystemTest", "TASK_SET_PARA9", "A test parameter set by a task", 0, "PARA9", "", "A test parameter more", 9d, "Bananas"));

		Thread.sleep(2000);
		
		azzert(monitoringListener.lastReceived.getName().equals("PARA9"), "The 'Set' parameter was received,");	
		Parameter out1 = (Parameter) monitoringListener.lastReceived;
		azzert(out1.getValue().doubleValue() == 9.0d, "Value was 9 as expected.");	
		azzert(out1.getIssuedBy().equals("TaskExecutor1") || out1.getIssuedBy().equals("TaskExecutor2"), "It was issued by one of the two task executors.");	
		
		
		monitoringListener.elements.clear();
		
		/** Create a repeatable Set. */
		Task task = new SetParameter("SystemTest", "TASK_SET_PARA9", "A test parameter set by a task", 0, "PARA9", "", "A test parameter more", 9d, "Bananas");
		task.setRepeat(5);
		task.setExecutionDelay(1000);
		injection.sendBody(task);

		Thread.sleep(10000);
		
		azzert(monitoringListener.elements.size() == 5, "Received 6 repetitions.");	

		
		
		/** Create a task for issuing a command. */
		Command command = new Command("SystemTest", "Any", "COM1", "A test command used to test SendCommand task.", 0, 0, null);
		injection.sendBody(new SendCommand("SystemTest", "TASK_SET_PARA9", "A test parameter set by a task", 0, command));

		Thread.sleep(2000);
		
		azzert(commandingListener.lastReceived.getName().equals("COM1"), "Received command.");	
		
	}
}
