package org.hbird.exchange.core;

import java.util.Comparator;

import org.hbird.exchange.core.Named;



public class NamedTimestampComperator implements Comparator<Named> {

	public int compare(Named lhs, Named rhs) {
		
		if (lhs.getTimestamp() < rhs.getTimestamp()) {
			return -1;
		}
		else if (lhs.getTimestamp() > rhs.getTimestamp()) {
			return 1;
		}
		
		return 0;
	}
}
