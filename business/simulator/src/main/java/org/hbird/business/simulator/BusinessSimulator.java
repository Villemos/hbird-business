package org.hbird.business.simulator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.InOnly;
import org.hbird.business.simulator.waveforms.Waveform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessSimulator {

	private final static Logger LOG = LoggerFactory.getLogger(BusinessSimulator.class);

	private List<Waveform> waveforms = new ArrayList<Waveform>();

	private long messageInterval = 1000;
	private String parameterName = "Sample Parameter";
	private String parameterDescription = "This is a sample parameter description";
	
	private int currentWaveform = 0;
	private int currentReading = 0;

	public void setMessageInterval(long messageInterval) {
		this.messageInterval = messageInterval;
	}

	public void addWaveform(Waveform waveform) {
		waveforms.add(waveform);
	}
	
	public void setWaveforms(List<Waveform> waveforms) {
		this.waveforms = waveforms;
	}

	@InOnly	
	public Message getNextValue(Exchange exchange) throws InterruptedException {
		
		Thread.sleep(messageInterval);
		
		// we have reached the end of the current waveform
		if (currentReading >= waveforms.get(currentWaveform).getReadings()) {
			currentReading = 0;
			currentWaveform++;
			LOG.debug("We have reached the end of the current waveform, switching to the next");
		}
		
		// we have reached the end of our waveforms
		if (currentWaveform >= waveforms.size()) {
			currentReading = 0;
			currentWaveform = 0;
			LOG.debug("We have reached the end of our waveforms, switching to the first");
		}
		
		Double value = waveforms.get(currentWaveform).nextValue();
		
		Message message = exchange.getIn();
		message.setHeader("Source", "Hummingbird Simulator");
		message.setHeader("Name", parameterName);
		message.setHeader("Description", parameterDescription);
		
		Date d = new Date(System.currentTimeMillis());
		DateFormat df = SimpleDateFormat.getDateTimeInstance();
		message.setHeader("Timestamp", df.format(d));
		
		message.setHeader("Value", value);
		
		message.setBody(value.toString());

		LOG.info("Sending message with value: " + value);
		exchange.setIn(message);	
		return message;
		
	}
}
