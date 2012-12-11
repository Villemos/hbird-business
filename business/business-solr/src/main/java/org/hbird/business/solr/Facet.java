package org.hbird.business.solr;

import java.util.HashMap;
import java.util.Map;

import org.hbird.exchange.core.Named;


/**
 * Data structure holding the definition of a facet. A facet is a specific field
 * with all the values associated with the field. 
 *
 */
public class Facet extends Named {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2469230224084701038L;

	/** The field for which the facet values are applicable. */
	public String field;
	
	/** Map keyed on facet value, with the value being a count of how often it occurs. */
	public Map<String, Long> values = new HashMap<String, Long>();
}
