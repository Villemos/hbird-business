package org.hbird.business.systemtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.State;

public class Listener {

	public List<Named> elements = new ArrayList<Named>();
	
	public Named lastReceived = null;
	
	public Map<String, State> states = new HashMap<String, State>();
	
	
	
	public void process(Named named) {
		elements.add(named);
		lastReceived = named;
		
		if (named instanceof State) {
			State state = (State) named;
			states.put(state.getName(), state);
		}
	}	
}

