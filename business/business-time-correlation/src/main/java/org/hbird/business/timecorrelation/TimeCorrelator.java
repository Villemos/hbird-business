package org.hbird.business.timecorrelation;

import java.util.Date;

/**
 * A time correlator can convert between ground and spacecraft time in either direction.
 * 
 * @author Mark
 * 
 */
public interface TimeCorrelator {

	long convertToSpacecraftTime(Date groundtime);

	Date convertToGroundTime(long spacecraftTime);
}
