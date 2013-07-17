package org.hbird.business.api.impl;

import org.hbird.business.api.IHbirdApi;

public abstract class AbstractHbirdApi implements IHbirdApi {
	protected String issuedBy;
	protected String destination;
	
	public AbstractHbirdApi(String issuedBy, String destination) {
		setIssuedBy(issuedBy);
		setDestination(destination);
	}

	@Override
	public String getIssuedBy() {
		return issuedBy;
	}

	@Override
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	@Override
	public String getDestination() {
		return destination;
	}

	@Override
	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public void dispose() throws Exception {
	}

}
