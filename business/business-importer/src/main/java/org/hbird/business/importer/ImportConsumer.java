/**
 * villemos solutions [space^] (http://www.villemos.com) 
 * Probe. Send. Act. Emergent solution. 
 * Copyright 2011 Gert Villemos
 * All Rights Reserved.
 * 
 * Released under the Apache license, version 2.0 (do what ever
 * you want, just dont claim ownership).
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of villemos solutions, and its suppliers
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to villemos solutions
 * and its suppliers and may be covered by European and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * 
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from villemos solutions.
 * 
 * And it wouldn't be nice either.
 * 
 */
package org.hbird.business.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.tasking.SetParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jmx.snmp.tasks.Task;

/**
 * The HelloWorld consumer.
 */
public class ImportConsumer extends ScheduledPollConsumer {

	private static final transient Logger LOG = LoggerFactory.getLogger(ImportConsumer.class);

	private final ImportEndpoint endpoint;

	public ImportConsumer(ImportEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
	}

	@Override
	protected int poll() throws Exception {

		File input = new File(endpoint.getFilename());

		if (input.exists() == false) {
			LOG.warn("File '" + input.getAbsolutePath() + "' does not exist.");
			return 0;
		}

		Workbook workbook = Workbook.getWorkbook(input);

		for (String sheetName : workbook.getSheetNames()) {
			List<Object> dataList = new ArrayList<Object>();

			/** Get the commands.*/
			Sheet bodySheet = workbook.getSheet("Commands");

			/** Iterate through the rows. */
			/** A starting row may have been set. */
			for (int row = 1; row < bodySheet.getRows(); row++) {

				/** Format
				 * 
				 *  String issuedBy, String name, String description, long releaseTime, long executionTime, List<Parameter> arguments
				 *   0.  issuedBy
				 *   1.  Command Name
				 *   2.  Command Description.
				 *   3.  releaseTime
				 *   4.  executionTime
				 *   5.  argumentName
				 *   6.  argumentValue
				 *   7.  lockstates
				 *   8.  taskName
				 *   9.  taskParameterValue
				 *   10. executionTime
				 * */

				String issuedBy = bodySheet.getCell(0, row).getContents();
				String commandName = bodySheet.getCell(1, row).getContents();
				String commandDescription = bodySheet.getCell(2, row).getContents();
				String releaseTime = bodySheet.getCell(3, row).getContents();
				String executionTime = bodySheet.getCell(4, row).getContents();

				List<Parameter> arguments = new ArrayList<Parameter>();
				int tempRow = row;
				while ((tempRow == row && bodySheet.getCell(5, row).getContents() != null) || bodySheet.getCell(0, tempRow).getContents() == null) {
					String argumentName = bodySheet.getCell(5, row).getContents();
					String argumentDescription = bodySheet.getCell(6, row).getContents();
					String argumentValue = bodySheet.getCell(7, row).getContents();
					String argumentUnit = bodySheet.getCell(8, row).getContents();	
					arguments.add(new Parameter("", argumentName, argumentDescription, argumentValue, argumentUnit));
				}

				List<String> lockstates = new ArrayList<String>();
				tempRow = row;
				while ((tempRow == row && bodySheet.getCell(5, row).getContents() != null) || bodySheet.getCell(0, tempRow).getContents() == null) {
					lockstates.add(bodySheet.getCell(9, row).getContents());
				}

				List<Task> tasks = new ArrayList<Task>();
				tempRow = row;
				while ((tempRow == row && bodySheet.getCell(5, row).getContents() != null) || bodySheet.getCell(0, tempRow).getContents() == null) {
					String taskName = bodySheet.getCell(10, row).getContents();
					String taskDescription = bodySheet.getCell(11, row).getContents();
					String taskType = bodySheet.getCell(12, row).getContents();
					String taskExecutionTime = bodySheet.getCell(13, row).getContents();
					if (taskType.equals("set")) {
						// tasks.add(new SetParameter("", taskName, taskDescription, Long.parseLong(taskExecutionTime)));
					}
					else if (taskType.equals("reflective")) {
						// tasks.add(new ReflectiveSetParameter("", taskName, taskDescription, Long.parseLong(taskExecutionTime)));
					}

				}
			}

		}
		return 1;
	}
}
