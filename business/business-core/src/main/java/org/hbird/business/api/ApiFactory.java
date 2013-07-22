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

import org.apache.camel.CamelContext;
import org.hbird.business.api.deprecated.IDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating API implementations.
 * 
 * Different implementations may exist, depending on the underlying technologies used.
 * 
 * @author Gert Villemos
 * 
 */
public class ApiFactory {

    protected static String dataAccessClass = System.getProperty("hbird.dataaccess.class", "org.hbird.business.archive.api.DataAccess");
    protected static String publishClass = System.getProperty("hbird.publish.class", "org.hbird.business.archive.api.Publish");
    protected static String catalogueClass = System.getProperty("hbird.catalogue.class", "org.hbird.business.archive.api.Catalogue");
    protected static String queueManagementClass = System.getProperty("hbird.queuemanagement.class", "org.hbird.business.queuemanagement.api.QueueManagerApi");
    protected static String partmanagerClass = System.getProperty("hbird.partmanager.class", "org.hbird.business.archive.api.PartManager");
    protected static String archiveManagerClass = System.getProperty("hbird.archivemanager.class", "org.hbird.business.archive.api.ArchiveManagement");
    protected static String orbitDataClass = System.getProperty("hbird.orbitdata.class", "org.hbird.business.navigation.orekit.OrbitDataApi");
    protected static String idBuilderClass = System.getProperty("hbird.idbuilder.class", "org.hbird.business.api.impl.DefaultIdBuilder");

    private static final Logger LOG = LoggerFactory.getLogger(ApiFactory.class);

    /**
     * Default private constructor to avoid instance creation.
     */
    private ApiFactory() {
    }

    public static synchronized IDataAccess getDataAccessApi(String issuedBy) {
        return (IDataAccess) createInstance(dataAccessClass, issuedBy);
    }

    public static synchronized IPublish getPublishApi(String issuedBy) {
        return (IPublish) createInstance(publishClass, issuedBy);
    }

    public static synchronized ICatalogue getCatalogueApi(String issuedBy) {
        return (ICatalogue) createInstance(catalogueClass, issuedBy);
    }

    public static synchronized IQueueManagement getQueueManagementApi(String issuedBy) {
        return (IQueueManagement) createInstance(queueManagementClass, issuedBy);
    }

    public static synchronized IPartManager getPartManagerApi(String issuedBy) {
        return (IPartManager) createInstance(partmanagerClass, issuedBy);
    }

    public static synchronized IArchiveManagement getArchiveManagerApi(String issuedBy) {
        return (IArchiveManagement) createInstance(archiveManagerClass, issuedBy);
    }

    public static synchronized IPointingData getOrbitDataApi(String issuedBy) {
        return (IPointingData) createInstance(orbitDataClass, issuedBy);
    }

    public static synchronized IDataAccess getDataAccessApi(String issuedBy, CamelContext context) {
        return (IDataAccess) createInstance(dataAccessClass, issuedBy, context);
    }

    public static synchronized IPublish getPublishApi(String issuedBy, CamelContext context) {
        return (IPublish) createInstance(publishClass, issuedBy, context);
    }

    public static synchronized ICatalogue getCatalogueApi(String issuedBy, CamelContext context) {
        return (ICatalogue) createInstance(catalogueClass, issuedBy, context);
    }

    public static synchronized IQueueManagement getQueueManagementApi(String issuedBy, CamelContext context) {
        return (IQueueManagement) createInstance(queueManagementClass, issuedBy, context);
    }

    public static synchronized IPartManager getPartManagerApi(String issuedBy, CamelContext context) {
        return (IPartManager) createInstance(partmanagerClass, issuedBy, context);
    }

    public static synchronized IArchiveManagement getArchiveManagerApi(String issuedBy, CamelContext context) {
        return (IArchiveManagement) createInstance(archiveManagerClass, issuedBy, context);
    }

    public static synchronized IPointingData getOrbitDataApi(String issuedBy, CamelContext context) {
        return (IPointingData) createInstance(orbitDataClass, issuedBy, context);
    }

    public static synchronized IdBuilder getIdBuilder() {
        return (IdBuilder) createInstance(idBuilderClass);
    }

    protected static synchronized Object createInstance(String clazz) {
        Object api = null;
        try {
            api = Class.forName(clazz).newInstance();
        }
        catch (Exception e) {
            LOG.error("Failed to create new instance of {}", clazz, e);
        }
        return api;
    }

    protected static synchronized Object createInstance(String clazz, String issuedBy) {
        Object api = null;
        try {
            api = Class.forName(clazz).getConstructor(String.class).newInstance(issuedBy);
        }
        catch (Exception e) {
            LOG.error("Failed to create new instance of {}", clazz, e);
        }
        return api;
    }

    protected static synchronized Object createInstance(String clazz, String issuedBy, CamelContext context) {
        Object api = null;
        try {
            api = Class.forName(clazz).getConstructor(String.class, CamelContext.class).newInstance(issuedBy, context);
        }
        catch (Exception e) {
            LOG.error("Failed to create new instance of class {}", clazz, e);
        }
        return api;
    }
}
