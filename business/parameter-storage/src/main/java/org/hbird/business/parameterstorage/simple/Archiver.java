/**
 * Licensed under the Apache License, Version 2.0. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0 or at this project's root.
 */

package org.hbird.business.parameterstorage.simple;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.springframework.jdbc.core.JdbcTemplate;

/*
 * Creates all tables necessary to store the received parameter in a database.
 */
public class Archiver {
	private Set<String> tableExists = new TreeSet<String>();
	private Map<String,String> sqlPreparedStatements = new HashMap<String,String>();
	private String[] sqlTableCount = {"SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '", "' OR TABLE_NAME = '", "';"};
	
	private JdbcTemplate template = null;

	/**
	 * The Constructor
	 * 
	 * @param database
	 * 			The database to store the parameters in.
	 */
	public Archiver(DataSource database) {
		template = new JdbcTemplate(database);
	}

	/**
	 * Stores the submitted parameter in the database. 
	 * 
	 * @param name
	 * 			(Is automatically extracted from header)
	 *			Name of the parameter, e.g. 'Elevation'. 
	 * @param timestamp
	 * 			(Is automatically extracted from header) 
	 *          Timestamp set by the satellite, e.g. '1302558974895'.
	 * @param body
	 * 			(Is automatically extracted from Body) 
	 *    	    Value of the parameter, e.g. an XML string. 
	 */
	public void store(@Header("name") String name,
			@Header("timestamp") Long timestamp,
			@Body String body) {
			
		String nameLowerCase = name.toLowerCase();
		
		name = nameLowerCase.replace(" ", "_");

		if (!tableExists.contains(name)) {
			//If tableExists doesn't contain the name of the table, the table might not exists...
			//but check the database to make sure.
			//Anyways, the statement to insert data into this table will be created.
			sqlPreparedStatements.put(name,
					"INSERT INTO " + name + " (timestamp, local_timestamp, body) values (?, ?, ?);");

			int numberOfTables = template.queryForInt(sqlTableCount[0] + name.toUpperCase() + sqlTableCount[1] + name.toLowerCase() + sqlTableCount[2]);
								
			if (numberOfTables == 0) {
				//Table really doesn't exists in the database. Create it.
			
				String createTableStatement = "CREATE TABLE " + name
						+ " (timestamp BIGINT, "
						+ "local_timestamp BIGINT, body varchar(1500), "
						+ "PRIMARY KEY (timestamp));\n";
				template.execute(createTableStatement);
			}

			tableExists.add(name);
		}

		// Insert data into database
		template.update(sqlPreparedStatements.get(name), new Object[] { timestamp,
				System.currentTimeMillis(), body });
	}
}
