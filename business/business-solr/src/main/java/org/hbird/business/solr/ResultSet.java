package org.hbird.business.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hbird.exchange.core.Named;

public class ResultSet {

	public List<Named> objects = new ArrayList<Named>();
	
	public List<Facet> facets = new ArrayList<Facet>();
	
	public Statistics statistics = new Statistics();
	
	public Map<Object, List<String>> highlights = new HashMap<Object, List<String>>();
	
	public List<Named> getResults() {
		List<Named> newList = new ArrayList<Named>();
		newList.addAll(objects);
		newList.addAll(facets);
		
		return newList;
	}
}
