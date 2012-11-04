package org.hbird.business.core;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.State;
import org.junit.Test;

public class FieldBasedSchedulerTest {

	@Test
	public void testSchedule() {
		State parameter = new State("issuesBy", "name", "description", "isStateOf", false);
		
		Map<String, Object> headers = new HashMap<String, Object>();
		
		FieldBasedScheduler processor = new FieldBasedScheduler();
		try {
			/** No timestamp field. */
			processor.setFieldName("TestField");
			processor.process(parameter, headers);
			
			assertFalse(headers.containsKey("name"));
			assertFalse(headers.containsKey("description"));
			assertFalse(headers.containsKey("datasetidentifier"));
			assertFalse(headers.containsKey("isStateOf"));
			assertFalse(headers.containsKey("issuedBy"));
			assertFalse(headers.containsKey("value"));
			assertFalse(headers.containsKey("timestamp"));
			assertFalse(headers.containsKey("unit"));
			assertFalse(headers.containsKey(processor.getHeaderField()));
			
			/** Set time in the past. */
			processor.setFieldName("timestamp");
			Date now = new Date();
			parameter.setTimestamp(now.getTime() - 5000l);
			processor.process(parameter, headers);
			assertFalse(headers.containsKey(processor.getHeaderField()));
			
			/** Set time in the future. */
			now = new Date();
			parameter.setTimestamp(now.getTime() + 5000l);
			processor.process(parameter, headers);
			assertTrue(headers.containsKey(processor.getHeaderField()));
			
			headers.clear();

			/** Change header field, then set time in the future. */
			processor.setHeaderField("TEST_HEADER");
			now = new Date();
			parameter.setTimestamp(now.getTime() + 5000l);
			processor.process(parameter, headers);
			assertTrue(headers.containsKey("TEST_HEADER"));

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
