package org.hbird.business.simulator.waveforms;

import java.util.Random;

public class RandomValue extends AbstractWaveform {
	
	private double min, max;
	private Random random;

	public RandomValue(double min, double max) {
		super(1);
		this.min = min;
		this.max = max;
		random = new Random();
	}

	public double nextValue() {
		// TODO Auto-generated method stub
		return random.nextDouble()*(max-min)+min;
	}

}
