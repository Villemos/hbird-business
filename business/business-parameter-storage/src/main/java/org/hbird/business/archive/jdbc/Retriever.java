/**
 * Licensed under the Apache License, Version 2.0. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0 or at this project's root.
 */

package org.hbird.business.archive.jdbc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Body;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/*
 * Fetches the stored parameters from the database and sends them to a 
 * specified destination.
 */
public class Retriever {
	String errorMessageFaultyInputstring = 
		"Input string to create sql query is faulty! Needs to be either\n"
		+ "'parametername' (String;String) or \n"
		+ "'parametername;starttime;endtime' (String;long;long)";
	String errorMessageIncorrectTimestamps = 
		"Input string to create sql query is faulty! The end-timestamp needs to be "
		+ "AFTER the start-timestamp.";
	
	private JdbcTemplate template = null;
	
	private String destination;

	@Autowired
	private ProducerTemplate producer = null;

	@Autowired
	private CamelContext context = null;

	/**
	 * Constructor. Extracts the connection to the database from
	 * the DataSource.
	 * 
	 * @param dataSource
	 * 			The database to retrieve the parameters from.
	 * @param destination
	 * 			Name of the destination to send the retrieved 
	 *   		parameters to, e.g. 'activemq:queue:replay'.
	 */
	public Retriever(DataSource dataSource, String destination) {
		template = new JdbcTemplate(dataSource);

		this.destination = destination;
	}

	/**
	 * 	Method to fetch the parameters from the database and send them to a 
	 *  specified destination.
	 * @param retrieverCommand
	 * 			(Is automatically extracted from body) 
	 * 			There are two possible input strings:
	 * 			1. 'parameter_name;start_timestamp;end_timestamp' (will restore the 
	 *			named parameters which have a timestamp between 'start_timestamp' and 'end_timestamp'
	 * 			2. 'parameter_name' (will restore all parameters with the given name)
	 * @throws IOException
	 */
	final public void fetchParameters(@Body String retrieverCommand) throws IOException {
		final int TIMESTAMP_OVERLAPPING = 1000;
		long startTimeStamp = 0;
		long endTimeStamp = 0;
		String parameterName = "";
		String[] command;

		if (!retrieverCommand.matches("^[A-Za-z][0-9A-Za-z-_]*(;([0-9]{1,13});([0-9]{1,13}))?$")) {
			throw new IOException(errorMessageFaultyInputstring + "\n###" + retrieverCommand);
		}

		// Splits the body into 3 single Strings if necessary. If
		// it only contains the name of the parameter, nothing happens
		command = retrieverCommand.split(";");

		switch (command.length) {
		case 1:
			startTimeStamp = 0;
			endTimeStamp = System.currentTimeMillis();
			parameterName = command[0];
			break;
		case 3:
			parameterName = command[0];
			startTimeStamp = Long.parseLong(command[1]);
			endTimeStamp = Long.parseLong(command[2]);
			break;
		}
		
		if(startTimeStamp >= endTimeStamp) {
			throw new IOException(errorMessageIncorrectTimestamps + "\n###" + retrieverCommand);			
		}
		
		// Ceate and run statements. Call 'processResults' to send the retrieved datasets.
		String sqlSelect;
		List<Map<String, Object>> result;

		//First statement: timestamp between 'start' and 'end - TIMESTAMP_OVERLAPPING'
		sqlSelect = "select * from " + parameterName + " where TIMESTAMP >= "
		+ startTimeStamp + " and TIMESTAMP < " + (endTimeStamp - TIMESTAMP_OVERLAPPING)
		+ ";";
		
		result = template.queryForList(sqlSelect);
		processResults(result);

		//Second statement: timestamp between 'end - TIMESTAMP_OVERLAPPING' and 'end'
		sqlSelect = "select * from " + parameterName + " where TIMESTAMP >= "
		+ (endTimeStamp - TIMESTAMP_OVERLAPPING) + " and TIMESTAMP < " + endTimeStamp
		+ ";";
		
		result = template.queryForList(sqlSelect);
		processResults(result);
	}

	/**
	 * Runs through the results, and creates and sends a message
	 * for every row in the dataset. 
	 * 
	 * @param result
	 * 			The List that contains all the stored parameters that
	 * 			are restored and shall be send to a queue/topic.
	 */
	private void processResults(List<Map<String,Object>> result) {
		Exchange exchange;
		
		Map<String,Object> element = null;
		
		//As long as datasets exist in the ResultSet, create a new exchange
		while (!result.isEmpty()) {
			element = result.remove(0);
			exchange = new DefaultExchange(context);
			exchange.getIn().setBody(element.get("body"));
			
			//Send the created exchange
			producer.send(destination, exchange);
		}
	}
}
