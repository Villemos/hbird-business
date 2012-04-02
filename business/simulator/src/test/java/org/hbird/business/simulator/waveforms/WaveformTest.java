package org.hbird.business.simulator.waveforms;

import static org.junit.Assert.*;

import org.junit.Test;

public class WaveformTest {

	@Test
	public void flatWaveform() {
		FlatWaveform fw = new FlatWaveform(1);
		assertEquals(1, fw.nextValue(), 0.001);
		assertEquals(1, fw.nextValue(), 0.001);
		assertEquals(1, fw.nextValue(), 0.001);
		assertEquals(1, fw.nextValue(), 0.001);

	}

	@Test
	public void linearWaveform() {
		LinearWaveform lw = new LinearWaveform(2, 120d, 10d);
		assertEquals(120.0, lw.nextValue(), 0.001);
		assertEquals(130.0, lw.nextValue(), 0.001);
		assertEquals(120.0, lw.nextValue(), 0.001);
		assertEquals(130.0, lw.nextValue(), 0.001);

	}
	
	@Test
	public void randomValue() {
		RandomValue rv = new RandomValue(0, 10);
		for (int i=0; i<10000; i++) {
			double value = rv.nextValue();
			assertTrue(value < 10);
			assertTrue(value > 0);
		}
	}
	
	@Test
	public void randomValueWithNegativeNumbers() {
		RandomValue rv = new RandomValue(-100, -50);
		for (int i=0; i<10000; i++) {
			double value = rv.nextValue();
			assertTrue(value > -100);
			assertTrue(value < -50);
		}
	}

	@Test
	public void linearWaveformWithFractions() {
		LinearWaveform lw = new LinearWaveform(5, 0d, 0.25d);
		assertEquals(0.0, lw.nextValue(), 0.001);
		assertEquals(0.25, lw.nextValue(), 0.001);
		assertEquals(0.5, lw.nextValue(), 0.001);
		assertEquals(0.75, lw.nextValue(), 0.001);
		assertEquals(1.0, lw.nextValue(), 0.001);
		assertEquals(0.0, lw.nextValue(), 0.001);

	}

	@Test
	public void sineWaveformTest() {
		/*
		 * Using values: A = 1, w = 0.5, p = 0
		 * 
		 * i.e. 1 * sin(x*0.5 + 0)
		 */
		Waveform sine = new SineWaveform(1, 0.5, 0);

		double expected[] = { 0, 0.4794, 0.8415, 0.9975, 0.9093, 0.5985, 0.1411, -0.3508, -0.7568, -0.9775 };

		for (int i = 0; i < 10; i++) {
			assertEquals(expected[i], sine.nextValue(), 0.0001);
		}

	}

	@Test
	public void sineWaveformMoreExoticValuesTest() {
		/*
		 * Using values: A = 9, w = 0.0, p = 10
		 * 
		 * i.e. 9 * sin(x*0.1 + 10)
		 */
		Waveform sine = new SineWaveform(9, 0.1, 10);
		double expected[] = { -4.8962, -5.6256, -6.2989, -6.9092, -7.4504, -7.9173, -8.305, -8.6097, -8.8284, -8.9589 };

		for (int i = 0; i < 10; i++) {
			assertEquals(expected[i], sine.nextValue(), 0.0001);
		}
	}
}
