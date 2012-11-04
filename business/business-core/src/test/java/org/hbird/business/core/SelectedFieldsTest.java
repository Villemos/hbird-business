package org.hbird.business.core;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.State;
import org.junit.Test;

public class SelectedFieldsTest {

	@Test
	public void testProcess() {
		State parameter = new State("issuesBy", "name", "description", "isStateOf", false);
		
		Map<String, Object> headers = new HashMap<String, Object>();
		
		SelectedFields processor = new SelectedFields();
		try {
			/** No fields set. All fields should be mapped. */
			processor.process(parameter, headers);
			
			assertTrue(parameter.getName().equals(headers.get("name")));
			assertTrue(parameter.getDescription().equals(headers.get("description")));
			assertTrue(parameter.getIsStateOf().equals(headers.get("isStateOf")));
			assertTrue(parameter.getIssuedBy().equals(headers.get("issuedBy")));
			assertTrue(parameter.getTimestamp() == (Long) headers.get("timestamp"));
			
			headers.clear();
			processor.fields.add("name");
			processor.fields.add("timestamp");
			processor.process(parameter, headers);
			
			assertTrue(parameter.getName().equals(headers.get("name")));
			assertFalse(headers.containsKey("description"));
			assertFalse(headers.containsKey("datasetidentifier"));
			assertFalse(headers.containsKey("isStateOf"));
			assertFalse(headers.containsKey("issuedBy"));
			assertFalse(headers.containsKey("value"));
			assertTrue(parameter.getTimestamp() == (Long) headers.get("timestamp"));
			assertFalse(headers.containsKey("unit"));
			
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
