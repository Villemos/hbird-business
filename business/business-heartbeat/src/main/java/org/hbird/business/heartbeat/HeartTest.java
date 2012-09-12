package org.hbird.business.heartbeat;

import static org.junit.Assert.*;

import org.hbird.exchange.heartbeat.Heartbeat;
import org.junit.Test;

public class HeartTest {

	@Test
	public void testProcess() {
		Heart heart = new Heart("test component", 5000);
		assertTrue(heart.componentId.equals("test component"));
		assertTrue(heart.period == 5000);
		
		Heartbeat beat = heart.process();
		assertTrue(beat.getIssuedBy().equals("test component"));
		assertTrue(beat.getTimestamp() != 0);
		assertTrue(beat.getNextBeat() != 0);
	}

}
