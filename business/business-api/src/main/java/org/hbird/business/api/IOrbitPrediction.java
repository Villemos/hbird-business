package org.hbird.business.api;

import java.util.List;

import org.hbird.exchange.core.DataSet;

public interface IOrbitPrediction {

	public DataSet requestOrbitPropagation(String satellite);
	public DataSet requestOrbitPropagation(String satellite, long from, long to);
	public DataSet requestOrbitPropagation(String satellite, String location, long from, long to);
	public DataSet requestOrbitPropagation(String satellite, List<String> locations, long from, long to);
	
	public void requestOrbitPropagationStream(String satellite);
	public void requestOrbitPropagationStream(String satellite, long from, long to);
	public void requestOrbitPropagationStream(String satellite, String location, long from, long to);
	public void requestOrbitPropagationStream(String satellite, List<String> locations, long from, long to);
}
