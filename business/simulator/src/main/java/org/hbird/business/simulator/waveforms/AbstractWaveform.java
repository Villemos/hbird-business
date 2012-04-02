package org.hbird.business.simulator.waveforms;

public abstract class AbstractWaveform implements Waveform {
	
	protected int readings;
	
	public AbstractWaveform(int readings) {
		this.readings = readings;
	}

	public int getReadings() {
		return readings;
	}

}
