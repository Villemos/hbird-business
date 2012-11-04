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
package org.hbird.business.metadatapublisher;

import java.util.List;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Camel 'Splitter' for loading definitions from a Spring bean file and inject
 * them into a route, for example to a ActiveMQ queue.
 * 
 * The name of the bean file must be set through the 'filename' attribute. The 
 * bean file must contain a list called 'objects' containing any number and 
 * combination of 'Named' objects. 
 * 
 * The publisher can be part of a route triggered at intervals, for example using
 * the camel timer.
 */
public class ObjectPublisher {
	
	/** The class logger. */
	protected static Logger logger = Logger.getLogger(ObjectPublisher.class);
	
	/** The name of the file to be loaded. The file may be changed on disk. */
	protected String filename = "components.xml";
	
	protected String listname = "components";

	public ObjectPublisher() {
	}

	/**
	 * Constructor.
	 * 
	 * @param filename Name of the file to be read at intervals.
	 */
	public ObjectPublisher(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Method to split the message. The returned message list is actually loaded
	 * from a Spring file, i.e. the original Exchange is ignored.
	 * 
	 * @return A list of messages, carrying as the body a command definition.
	 */
	@Handler
	public List<Object> process() {

		logger.info("Loading 'Named' objects from file.");
		
		/** Load the definitions from the spring bean file. */
		BeanFactory factory = new FileSystemXmlApplicationContext (filename);
		List<Object> objects = (List<Object>) factory.getBean(listname);
		
		return objects;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getListname() {
		return listname;
	}

	public void setListname(String listname) {
		this.listname = listname;
	}
}
