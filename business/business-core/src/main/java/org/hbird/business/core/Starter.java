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

public class Starter {

	private static org.apache.log4j.Logger LOG = Logger.getLogger("main");

	protected Main main = null;	

	public static void main(String[] args) throws Exception {
		Starter starter = new Starter();
		starter.boot();
	}
	
	protected void boot() throws Exception {
		LOG.info("Starting Hummingbird based system.");
				
		main = new Main();
		main.enableHangupSupport();
		
		/** Read the configuration file as the first argument. If not set, then we try the default name. */
		String assemblyFile = System.getProperty("hbird.assembly") == null ? "classpath:main.xml" : System.getProperty("hbird.assembly");

		LOG.info("Reading assembly file '" + assemblyFile + "'");
		new FileSystemXmlApplicationContext(assemblyFile);

		main.run();
	}	
}