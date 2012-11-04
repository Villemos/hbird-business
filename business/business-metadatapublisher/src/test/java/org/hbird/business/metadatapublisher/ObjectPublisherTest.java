package org.hbird.business.metadatapublisher;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ObjectPublisherTest {

	@Test
	public void testProcess() {
		ObjectPublisher publisher = new ObjectPublisher();
		publisher.setFilename("src/test/resources/components.xml");
		List<Object> list = publisher.process();
		assertTrue(list.size() == 3);
	
		publisher.setListname("components2");
		list = publisher.process();
		assertTrue(list.size() == 2);
	}
}
