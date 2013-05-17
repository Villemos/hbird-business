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
package org.hbird.business.navigation.orekit;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.ApiFactory;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.IPublish;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.navigation.ContactEventComponent;

/**
 * Component builder to create a Navigation Component
 * 
 * @author Gert Villemos
 * 
 */
public class ContactEventComponentDriver extends SoftwareComponentDriver {

    @Override
    public void doConfigure() {

        ContactEventComponent com = (ContactEventComponent) entity;
        String id = com.getID();
        CamelContext camelContext = com.getContext();

        IDataAccess dao = ApiFactory.getDataAccessApi(id, camelContext);
        IPublish publish = ApiFactory.getPublishApi(id, camelContext);
        IdBuilder idBuilder = ApiFactory.getIdBuilder();
        ICatalogue catalogue = ApiFactory.getCatalogueApi(id, camelContext);
        ContactEventBean bean = new ContactEventBean(com, dao, publish, idBuilder, catalogue);

        ProcessorDefinition<?> route = from(addTimer(com.getID(), com.getExecutionDelay())).bean(bean, "execute");
        addInjectionRoute(route);
    }
}
