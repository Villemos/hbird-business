package org.hbird.business.simulator.waveforms;

/**
 * Based upon the formula:
 * 
 * <pre>
 * A.sin(wt + p)
 * </pre>
 * 
 * where A is amplitude, w is angular frequency, t is time, and p is phase.
 * 
 * @author Mark Doyle
 * 
 */
public class SineWaveform extends AbstractWaveform {

	long amplitude;
	double angularFrequency;
	int phase;
	long time = 0;
	double yIntercept = 0;

	double startValue = 0;

	public SineWaveform(final long amplitude, final double angularFrequency, final int phase, final double yIntercept) {
		super(1);
		this.amplitude = amplitude;
		this.angularFrequency = angularFrequency;
		this.phase = phase;
		this.yIntercept = yIntercept;
	}
	
	public SineWaveform(final long amplitude, final double angularFrequency, final int phase) {
		super(1);
		this.amplitude = amplitude;
		this.angularFrequency = angularFrequency;
		this.phase = phase;
	}

	public double nextValue() {
		double currentValue = amplitude * Math.sin(time * angularFrequency + phase) + yIntercept;
		time++;
		return currentValue;
	}

}
