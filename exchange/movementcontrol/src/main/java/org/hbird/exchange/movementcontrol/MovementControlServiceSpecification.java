package org.hbird.exchange.movementcontrol;

import java.util.ArrayList;
import java.util.List;

import org.hbird.exchange.core.ServiceSpecification;

public class MovementControlServiceSpecification extends ServiceSpecification {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4887186718526187158L;

	{
		this.name = "Movement Control Service";
		this.description = "The movement control service control some vehicls movement.";
		
		List<Object> messages = new ArrayList<Object>();
		messages.add(new MoveBackward());
		messages.add(new MoveForward());
		messages.add(new Stop());
		messages.add(new TurnLeft());
		messages.add(new TurnRight());
		this.logicalIn.put("control", messages);
	}
	
}
