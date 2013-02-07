package org.hbird.exchange.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A dataset is a collection of Named objects.
 * 
 * The dataset will 
 * 
 * @author Gert Villemos
 *
 */
public class DataSet extends Named implements IGenerationTimestamped, ILocationSpecific, ISatelliteSpecific {

	private static final long serialVersionUID = 1999602415313253135L;

	/** The set of data that this data set is carrying. */
	protected List<Named> dataset = new ArrayList<Named>();

	protected String location;
	
	protected String satellite;
	
	public DataSet(String issuedBy, String name, String type, String description, long generationTime, String datasetidentifier) {
		super(issuedBy, name, type, description, generationTime, datasetidentifier);
	}

	/**
	 * Add a Named object to this dataset.
	 * 
	 * @param data The Named object to be added.
	 */
	public void addData(Named data) {
		data.setDatasetidentifier(datasetidentifier);
		if (data instanceof IGenerationTimestamped) {
			((IGenerationTimestamped) data).setGenerationTime(timestamp);
		}
		dataset.add(data);
	}

	public List<Named> getDataset() {
		return dataset;
	}

	public void setDataset(List<Named> dataset) {
		for (Named data : dataset) {
			addData(data);
		}
	}

	public long getGenerationTime() {
		return timestamp;
	}

	public void setGenerationTime(long generationtime) {
		this.timestamp = generationtime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSatellite() {
		return satellite;
	}

	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	
	
}
