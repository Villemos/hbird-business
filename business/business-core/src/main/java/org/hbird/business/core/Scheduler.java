package org.hbird.business.core;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.hbird.exchange.core.Scheduled;

public class Scheduler {

	public void schedule(@Body Object body, @Headers Map<String, Object> headers) {
		if (body instanceof Scheduled) {
			headers.put("AMQ_SCHEDULED_DELAY", ((Scheduled) body).getDelay());
		}
	}
}
