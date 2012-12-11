package org.hbird.business.solr;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hbird.business.solr.Facet;
import org.hbird.business.solr.ResultSet;
import org.hbird.exchange.core.Named;

import com.thoughtworks.xstream.XStream;

public class Utilities {
	
	private static final transient Logger LOG = LoggerFactory.getLogger(Utilities.class);

	protected static XStream xstream = new XStream();
	
	public static synchronized ResultSet getResultSet(QueryResponse response, int rows) {

		ResultSet set = new ResultSet();

		for (SolrDocument document : response.getResults()) {
			String inputString = (String) document.get("serialization");
			inputString = inputString.replaceAll("<!\\[CDATA\\[", "").replaceAll("\\]\\]", "");
			try {
				set.objects.add((Named) xstream.fromXML(inputString));
			} catch (Exception e) {
				e.printStackTrace();
			}		}

		set.facets = new ArrayList<Facet>();
		if (response.getFacetFields() != null) {
			for (FacetField facetfield :  response.getFacetFields()) {
				Facet facet = new Facet();
				facet.field = facetfield.getName();

				if (facetfield.getValues() != null) {
					for (Count count : facetfield.getValues()) {
						facet.values.put(count.getName(), count.getCount());
					}
				}
				set.facets.add(facet);
			}
		}

		set.statistics.queryTime = response.getQTime();
		set.statistics.totalFound = response.getResults().getNumFound();
		set.statistics.totalRequested = rows;
		set.statistics.totalReturned = response.getResults().size();
		set.statistics.maxScore = response.getResults().getMaxScore() == null ? 0f : response.getResults().getMaxScore();

		return set;
	}

	protected static Map<String, Field> getAllFields(Class clazz, Map<String, Field> fields) {
		/** See if there is a super class. */
		if (clazz.getSuperclass() != null) {
			getAllFields(clazz.getSuperclass(), fields);
		}

		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			fields.put(field.getName(), field);
		}

		return fields;
	}

	public static boolean isPrimitive(Object value) {
		return value instanceof String || 
		value instanceof Boolean ||
		value instanceof Long ||
		value instanceof Double ||
		value.getClass().isPrimitive();
	}
}
