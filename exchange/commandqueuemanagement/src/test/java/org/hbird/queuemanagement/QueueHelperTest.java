package org.hbird.queuemanagement;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class QueueHelperTest {

	@Test
	public void testGetProperties() {
		Map<String, String> properties = QueueHelper.getProperties("{scheduledJobId=ID:Admin-VAIO-53276-1355954904327-27:1:1:1:1, timestamp=1355954911269, deliverytime=1355954921269, name=CommandContainerREQ_1, breadcrumbId=ID-Admin-VAIO-53275-1355954903135-1-46, issuedBy=SystemTest, type=CommandContainer}");
		assertTrue(properties.size() == 7);
		assertTrue(properties.containsKey("scheduledJobId"));	
	}

}
