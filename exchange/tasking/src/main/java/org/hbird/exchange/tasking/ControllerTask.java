package org.hbird.exchange.tasking;

import java.util.List;

import org.hbird.exchange.core.Named;

/**
 * A controller task is a task that is used to control something. It is thus typically a 
 * enduring task that 
 * <li>Need to do something on first execution, such as initializing or validating the current state.</li>
 * <li>Need to do something at intervals, for example propagate an orbit or validate that the state of the system is ok.</li>
 * 
 * This abstract baseclass will be overloaded by specific controllers in the business modules.
 * 
 * @author Gert Villemos
 *
 */
public abstract class ControllerTask extends Task {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9092747466161850037L;

	public ControllerTask(String issuedBy, String name, String description, long executionDelay) {
		super(issuedBy, name, description, executionDelay);
	}

	@Override
	public List<Named> execute() {
		
		List<Named> elements = null;
		if (count == 0) {
			elements = onFirstExecution();			
		}
		else {
			elements = onExecution();
		}
		
		/** Add this task so it is also scheduled. */
		elements.add(reschedule());	
		return elements;
	}
	
	/**
	 * Method that will be called the first time the task executes. The 'onExecution' method will
	 * not be called automatically, but can be called from this method implementation if needed.
	 * 
	 * This method should be used to perform any necesarry initialization, startup, consistency check,
	 * clean up, etc needed prior to scheduling the controlled task for repeated execution. 
	 */
	abstract protected List<Named> onFirstExecution();
		
	/**
	 * Method to be called on all following executions of this task.
	 */
	abstract protected List<Named> onExecution();
}
