package org.hbird.business.simulator;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.hbird.business.simulator.waveforms.Waveform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simulator implements Runnable {

	private final static Logger LOG = LoggerFactory.getLogger(Simulator.class);

	@EndpointInject(uri="direct:simMessages")
	ProducerTemplate template;
	
	private List<Waveform> waveforms = new ArrayList<Waveform>();

	private boolean run;

	private long messageInterval = 1000;

	public void setMessageInterval(long messageInterval) {
		this.messageInterval = messageInterval;
	}

	public void addWaveform(Waveform waveform) {
		waveforms.add(waveform);
	}
	
	public void setWaveforms(List<Waveform> waveforms) {
		this.waveforms = waveforms;
	}

	public void sendMessage(Object value) {
		template.sendBodyAndHeader(value, "Source", "Hummingbird Simulator");
	}

	public void stopSimulator() {
		run = false;
	}

	public void run() {
		run = true;

		// File file = new File("/dev/video0");
		// // byte[] image = FileUtils.readFileToByteArray(file);
		// byte[] bytes = new byte[640 * 480 * 3];
		// BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		//
		// FileInputStream is = null;
		// try {
		// is = new FileInputStream(file);
		// } catch (FileNotFoundException e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }

		while (run) {
			
			LOG.info("Simulator started.");

			for (Waveform waveform : waveforms) {
				for (int i = 0; i < waveform.getReadings() && run == true; i++) {

					sendMessage(waveform.nextValue());

					try {
						Thread.sleep(messageInterval);
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}

}
