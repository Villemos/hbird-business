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
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IOrbitalDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.navigation.ContactEventComponent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Component builder to create a Navigation Component
 * 
 * @author Gert Villemos
 * 
 */
public class ContactEventComponentDriver extends SoftwareComponentDriver<ContactEventComponent> {
	protected IOrbitalDataAccess dao;
	protected IdBuilder idBuilder;
	protected ICatalogue catalogue;
	
	@Autowired
	public ContactEventComponentDriver(IOrbitalDataAccess dao, IPublisher publisher, IdBuilder idBuilder, ICatalogue catalogue) {
		super(publisher);
		
		this.dao = dao;
		this.idBuilder = idBuilder;
		this.catalogue = catalogue;
	}

    @Override
    public void doConfigure() {

        ContactEventComponent com = entity;
        String id = com.getID();
        CamelContext camelContext = com.getContext();

        ContactEventBean bean = new ContactEventBean(com, dao, publisher, idBuilder, catalogue);
        
        ProcessorDefinition<?> route = from(addTimer(com.getID(), com.getExecutionDelay())).bean(bean, "execute").bean(publisher, "publish");
    }
}
