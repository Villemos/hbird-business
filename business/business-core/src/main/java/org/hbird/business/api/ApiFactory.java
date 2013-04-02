package org.hbird.business.api;

public class ApiFactory {

	static protected String dataAccessClass = System.getProperty("hbird.dataaccess.class", "org.hbird.business.archive.api.DataAccess");
	static protected String publishClass = System.getProperty("hbird.publish.class", "org.hbird.business.archive.api.Publish");
	static protected String catalogueClass = System.getProperty("hbird.catalogue.class", "org.hbird.business.archive.api.Catalogue");
	static protected String orbitPredictionClass = System.getProperty("hbird.orbitprediction.class", "org.hbird.business.navigation.api.OrbitPropagation");
	static protected String queueManagementClass = System.getProperty("hbird.queuemanagement.class", "org.hbird.business.queuemanagement.api.QueueManagerApi");
	static protected String partmanagerClass = System.getProperty("hbird.partmanager.class", "org.hbird.business.archive.api.PartManager");
	
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
