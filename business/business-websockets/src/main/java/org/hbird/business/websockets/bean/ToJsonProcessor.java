package org.hbird.business.websockets.bean;


import java.util.UUID;

import org.apache.camel.Body;
import org.apache.log4j.spi.LoggingEvent;
import org.hbird.exchange.core.Event;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class ToJsonProcessor {

    private final GsonBuilder builder = new GsonBuilder();

    protected Gson gson = builder.create();

    public ToJsonProcessor() {
        builder.serializeSpecialFloatingPointValues();
    }
    
    public class LogEntry {
    	
    	public LogEntry(LoggingEvent event) {
    		this.timestamp = event.getTimeStamp();
    		this.severity = event.getLevel().toString();
    		this.text = event.getRenderedMessage();
    	}
    	
    	public String id = UUID.randomUUID().toString();
    	public long timestamp;
    	public String severity;
    	public String text;
    }

    public class ParameterEntry {
    	
    	public ParameterEntry(Parameter parameter) {
    		this.name = parameter.getName();
    		this.timestamp = parameter.getTimestamp();
    		this.value = parameter.getValue().toString();
    		this.unit = parameter.getUnit();
    		this.issuedBy = parameter.getIssuedBy();
    		this.description = parameter.getDescription();
    	}
    	
    	public String name;
    	public long timestamp;
    	public String value;
    	public String unit;
    	public String issuedBy;
    	public String description;
    }

    public class EventEntry {
    	
    	public EventEntry(Event event) {
    		this.name = event.getName();
    		this.timestamp = event.getTimestamp();
    		this.description = event.getDescription();
    		this.issuedBy = event.getIssuedBy();
    	}

    	public String id = UUID.randomUUID().toString();
    	public String name;
    	public long timestamp;
    	public String issuedBy;
    	public String description;
    }

    public class StateEntry {
    	
    	public StateEntry(State state) {
    		this.name = state.getName();
    		this.timestamp = state.getTimestamp();
    		this.description = state.getDescription();
    		this.issuedBy = state.getIssuedBy();
    		this.value = state.getValue().toString();
    		this.isStateOf = state.getIsStateOf();
    	}

    	public String id = UUID.randomUUID().toString();
    	public String name;
    	public long timestamp;
    	public String issuedBy;
    	public String value;
    	public String isStateOf;
    	public String description;
    }

    /** @{inheritDoc}. */
    public String process(@Body Object body) throws Exception {
    	String encode = "";
    	if (body instanceof LoggingEvent) {
    		encode = gson.toJson(new LogEntry((LoggingEvent) body));
    	}
    	else if (body instanceof Parameter) {
    		encode = gson.toJson(new ParameterEntry((Parameter) body));
    	}
    	else if (body instanceof Event) {
    		encode = gson.toJson(new EventEntry((Event) body));
    	}
    	else if (body instanceof State) {
    		encode = gson.toJson(new StateEntry((State) body));
    	}
    	
    	return encode;
    }
}
