/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.core;

import org.apache.camel.Main;
import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Provides a standard main method.
 * 
 * The Starter can be used to start hbird assemblies. The asssembly is defined in an
 * XML file following the Camel format. The file to be used is either defined using the
 * 'hbird.assembly' system property or by putting a 'main.xml' in the classpath.
 * 
 * @author Gert Villemos
 *
 */
public class Starter {

	/** Class logger */
	private static org.apache.log4j.Logger LOG = Logger.getLogger("main");

	
	/**
	 * The main method
	 * 
	 * @param args Arguments are NOT used
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Starter starter = new Starter();
		starter.boot();
	}
	
	/**
	 * Method to read the XML assembly file and run the main
	 * 
	 * @throws Exception
	 */
	protected void boot() throws Exception {
		LOG.info("Starting Hummingbird based system.");
	
		Main main = new Main();
		main.enableHangupSupport();
		
		/** Read the configuration file as the first argument. If not set, then we try the default name. */
		String assemblyFile = System.getProperty("hbird.assembly") == null ? "classpath:main.xml" : System.getProperty("hbird.assembly");

		LOG.info("Reading assembly file '" + assemblyFile + "'");
		new FileSystemXmlApplicationContext(assemblyFile);

		/** Configure log4j to allow dynamic changes to the log4j file. */
		String log4jFile = System.getProperty("log4j.configuration");
		if (log4jFile != null) {
			org.apache.log4j.PropertyConfigurator.configureAndWatch(log4jFile, 5000);
		}
		
		main.run();
	}	
}