package org.hbird.business.core;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.StateParameter;
import org.junit.Test;

public class SelectedFieldsTest {

	@Test
	public void testProcess() {
		StateParameter parameter = new StateParameter("issuesBy", "name", "description", "isStateOf", false);
		
		Map<String, Object> headers = new HashMap<String, Object>();
		
		SelectedFields processor = new SelectedFields();
		try {
			/** No fields set. All fields should be mapped. */
			processor.process(parameter, headers);
			
			assertTrue(parameter.getName().equals(headers.get("name")));
			assertTrue(parameter.getDescription().equals(headers.get("description")));
			assertTrue(parameter.getDatasetidentifier() == (Long) headers.get("datasetidentifier"));
			assertTrue(parameter.getIsStateOf().equals(headers.get("isStateOf")));
			assertTrue(parameter.getIssuedBy().equals(headers.get("issuedBy")));
			assertTrue(parameter.getStateValue() == (Boolean) headers.get("value"));
			assertTrue(parameter.getTimestamp() == (Long) headers.get("timestamp"));
			assertTrue(parameter.getUnit().equals(headers.get("unit")));
			
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
