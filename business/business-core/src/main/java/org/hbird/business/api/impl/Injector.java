package org.hbird.business.api.impl;

import org.apache.camel.CamelContext;
import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IPublisher;
import org.hbird.exchange.core.EntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Injector extends AbstractHbirdApi implements IPublisher {
	private Logger LOG = LoggerFactory.getLogger(Injector.class);
	
	private class InjectApi extends HbirdApi {
		public InjectApi(String issuedBy, String destination) {
			super(issuedBy, destination);
		}
		
		public InjectApi(String issuedBy, String destination, CamelContext context) {
			super(issuedBy, destination, context);
		}
	}
	
	private InjectApi injector;
	private IPublisher delegate;

	public Injector(String issuedBy, String destination, IPublisher delegate) {
		super(issuedBy, destination);

		injector = new InjectApi(issuedBy, destination);
		this.delegate = delegate;
	}
	
	public Injector(String issuedBy, String destination, CamelContext context, IPublisher delegate) {
		super(issuedBy, destination);

		injector = new InjectApi(issuedBy, destination, context);
		this.delegate = delegate;
	}
	
	@Override
	public EntityInstance publish(EntityInstance object) throws Exception {
		LOG.trace("publishing " + object);

		if(object == null) { // E.g. limit checker can send null
			return null;
		}

		EntityInstance saved = delegate.publish(object);
		
		return publishToAMQ(saved);
	}
	
	protected EntityInstance publishToAMQ(EntityInstance object) {
		return injector.publish(object);
	}
	
	@Override
	public void dispose() throws Exception {
		//injector.dispose();
		//delegate.dispose(); //XXX
	}

	@Override
	public void commit(String ID) throws Exception {
		
	}

}
