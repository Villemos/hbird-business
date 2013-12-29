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

import org.apache.camel.model.ProcessorDefinition;
import org.hbird.business.api.IOrbitalDataAccess;
import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.navigation.OrbitPropagationComponent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Gert Villemos
 * 
 */
public class OrbitPropagationComponentDriver extends SoftwareComponentDriver<OrbitPropagationComponent> {
	protected IOrbitalDataAccess dao;
	protected IdBuilder naming;
	
	@Autowired
	public OrbitPropagationComponentDriver(IOrbitalDataAccess dao, IPublisher publisher, IdBuilder naming) {
		super(publisher);
		
		this.dao = dao;
		this.naming = naming;
	}

    /**
     * @see org.hbird.business.core.SoftwareComponentDriver#doConfigure()
     */
    @Override
    protected void doConfigure() {
        OrbitPropagationComponent com = entity;
        String id = com.getID();

        OrbitPropagationBean bean = new OrbitPropagationBean(com, dao, publisher, naming);

        from(addTimer(com.getID(), com.getExecutionDelay())).bean(bean, "execute").bean(publisher, "publish");
    }
}
