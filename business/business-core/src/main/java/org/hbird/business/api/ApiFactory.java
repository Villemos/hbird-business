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
package org.hbird.business.api;


/**
 * Factory class for creating API implementations. 
 * 
 * Different implementations may exist, depending on the underlying technologies used.
 * 
 * @author Gert Villemos
 *
 */
public class ApiFactory {

	static protected String dataAccessClass = System.getProperty("hbird.dataaccess.class", "org.hbird.business.archive.api.DataAccess");
	static protected String publishClass = System.getProperty("hbird.publish.class", "org.hbird.business.archive.api.Publish");
	static protected String catalogueClass = System.getProperty("hbird.catalogue.class", "org.hbird.business.archive.api.Catalogue");
	static protected String orbitPredictionClass = System.getProperty("hbird.orbitprediction.class", "org.hbird.business.navigation.api.OrbitPropagation");
	static protected String queueManagementClass = System.getProperty("hbird.queuemanagement.class", "org.hbird.business.queuemanagement.api.QueueManagerApi");
	static protected String partmanagerClass = System.getProperty("hbird.partmanager.class", "org.hbird.business.archive.api.PartManager");
	static protected String archiveManagerClass = System.getProperty("hbird.archivemanager.class", "org.hbird.business.archive.api.ArchiveManagement");
	
	static public synchronized IDataAccess getDataAccessApi(String issuedBy) {
		return (IDataAccess) createInstance(dataAccessClass, issuedBy);
	}

	static public synchronized IPublish getPublishApi(String issuedBy) {
		return (IPublish) createInstance(publishClass, issuedBy);
	}

	static public synchronized ICatalogue getCatalogueApi(String issuedBy) {
		return (ICatalogue) createInstance(catalogueClass, issuedBy);
	}

	static public synchronized IOrbitPrediction getOrbitPredictionApi(String issuedBy) {
		return (IOrbitPrediction) createInstance(orbitPredictionClass, issuedBy);
	}

	static public synchronized IQueueManagement getQueueManagementApi(String issuedBy) {
		return (IQueueManagement) createInstance(queueManagementClass, issuedBy);
	}

	static public synchronized IPartManager getPartManagerApi(String issuedBy) {
		return (IPartManager) createInstance(partmanagerClass, issuedBy);
	}

	static public synchronized IArchiveManagement getArchiveManagerApi(String issuedBy) {
		return (IArchiveManagement) createInstance(archiveManagerClass, issuedBy);
	}

	static protected synchronized Object createInstance(String clazz, String issuedBy) {
		Object api = null;
		
		try {
			api = Class.forName(clazz).getConstructor(String.class).newInstance(issuedBy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return api;
	}
}
