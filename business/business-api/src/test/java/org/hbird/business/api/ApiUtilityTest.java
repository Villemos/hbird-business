package org.hbird.business.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hbird.exchange.core.Issued;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.junit.Test;

public class ApiUtilityTest {

	@Test
	public void testSortParametersAndState() {
		
		/** Create the test data*/
		List<Issued> testData = new ArrayList<Issued>();
		
        testData.add(new Parameter("", "PARA1", "", 1, "", 1));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 1));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 1));

        testData.add(new Parameter("", "PARA1", "", 1, "", 2));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 2));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 2));

        testData.add(new Parameter("", "PARA2", "", 2, "", 3));
        testData.add(new State("", "STATE1_OF_PARA2", "","PARA2", true, 3));

        testData.add(new Parameter("", "PARA1", "", 1, "", 4));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 4));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 4));

        testData.add(new Parameter("", "PARA1", "", 1, "", 5));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 5));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 5));

        testData.add(new Parameter("", "PARA2", "", 1, "", 6));
        testData.add(new State("", "STATE1_OF_PARA2", "","PARA2", true, 6));

        testData.add(new Parameter("", "PARA3", "", 1, "", 7));

        testData.add(new Parameter("", "PARA1", "", 1, "", 8));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 8));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 8));

        testData.add(new Parameter("", "PARA1", "", 1, "", 9));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 9));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 9));

        testData.add(new Parameter("", "PARA4", "", 1, "", 10));
        testData.add(new State("", "STATE1_OF_PARA4", "","PARA4", true, 10));

        testData.add(new Parameter("", "PARA5", "", 1, "", 11));

        testData.add(new Parameter("", "PARA1", "", 1, "", 12));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 12));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 12));

        testData.add(new Parameter("", "PARA2", "", 1, "", 13));
        testData.add(new State("", "STATE1_OF_PARA2", "","PARA2", true, 13));

        testData.add(new Parameter("", "PARA2", "", 1, "", 14));
        testData.add(new State("", "STATE1_OF_PARA2", "","PARA2", true, 14));

        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 15));
        testData.add(new Parameter("", "PARA1", "", 1, "", 15));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 15));

        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 16));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 16));
        testData.add(new Parameter("", "PARA1", "", 1, "", 16));

        testData.add(new Parameter("", "PARA3", "", 1, "", 17));

        testData.add(new Parameter("", "PARA1", "", 1, "", 18));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 18));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 18));

        testData.add(new Parameter("", "PARA3", "", 1, "", 19));

        testData.add(new Parameter("", "PARA1", "", 1, "", 20));
        testData.add(new State("", "STATE1_OF_PARA1", "","PARA1", true, 20));
        testData.add(new State("", "STATE2_OF_PARA1", "","PARA1", true, 20));

        
		Map<Parameter, List<State>> result = ApiUtility.sortParametersAndState(testData);
		
		Iterator<Entry<Parameter, List<State>>> it = result.entrySet().iterator();
		
		assertEntry(it.next(), "PARA1", 1, 2);
		assertEntry(it.next(), "PARA1", 2, 2);
		assertEntry(it.next(), "PARA2", 3, 1);		
		assertEntry(it.next(), "PARA1", 4, 2);
		assertEntry(it.next(), "PARA1", 5, 2);
		assertEntry(it.next(), "PARA2", 6, 1);		
		assertEntry(it.next(), "PARA3", 7, 0);		
		assertEntry(it.next(), "PARA1", 8, 2);
		assertEntry(it.next(), "PARA1", 9, 2);
		assertEntry(it.next(), "PARA4", 10, 1);
		assertEntry(it.next(), "PARA5", 11, 0);		
		assertEntry(it.next(), "PARA1", 12, 2);
		assertEntry(it.next(), "PARA2", 13, 1);		
		assertEntry(it.next(), "PARA2", 14, 1);	
		assertEntry(it.next(), "PARA1", 15, 2);
		assertEntry(it.next(), "PARA1", 16, 2);
		assertEntry(it.next(), "PARA3", 17, 0);		
		assertEntry(it.next(), "PARA1", 18, 2);
		assertEntry(it.next(), "PARA3", 19, 0);		
		assertEntry(it.next(), "PARA1", 20, 2);
		
	}

	protected void assertEntry(Entry<Parameter, List<State>> entry, String name, long timestamp, int numStates) {
		assertTrue(entry.getKey().getName().equals(name));
		assertTrue(entry.getKey().getTimestamp() == timestamp);
		assertTrue(entry.getValue().size() == numStates);
		
		for (State state : entry.getValue()) {
			assertTrue(state.getTimestamp() == timestamp);
			assertTrue(state.getIsStateOf().equals(name));
		}
	}
}
