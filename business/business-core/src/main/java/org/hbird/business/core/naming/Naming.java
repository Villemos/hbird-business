package org.hbird.business.core.naming;

public interface Naming {

	
	String createAbsoluteName(Base base, String source, String relativeName);

	String createAbsoluteName(String ... parts);
}
