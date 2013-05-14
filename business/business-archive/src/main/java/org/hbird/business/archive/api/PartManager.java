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
package org.hbird.business.archive.api;

import org.apache.camel.CamelContext;
import org.hbird.business.api.IPartManager;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.interfaces.IStartableEntity;

/**
 * API for accessing information about Parts
 * 
 * @author Gert Villemos
 *
 */
public class PartManager extends Publish implements IPartManager {

	/**
	 * Constructor. 
	 * 
	 * @param issuedBy The name/ID of the component that is using the API to send requests.
	 * 
	 * TODO Remove the hardcoded destination
	 */
	public PartManager(String issuedBy) {
		super(issuedBy);
		this.destination = "Configurator";
	}

	public PartManager(String issuedBy, CamelContext context) {
		super(issuedBy, context);
		this.destination = "Configurator";
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPartManager#start(org.hbird.exchange.interfaces.IStartablePart)
	 */
	@Override
	public void start(IStartableEntity part) {
		StartComponent request = new StartComponent(getID());
		request.setIssuedBy(issuedBy);
		request.setPart(part);
		publish(request);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPartManager#stop(java.lang.String)
	 */
	@Override
	public void stop(String partName) {
		StopComponent request = new StopComponent(getID());
		request.setIssuedBy(issuedBy);
		request.setComponent(partName);
		publish(request);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPartManager#resolveParent(org.hbird.exchange.interfaces.IPart)
	 */
	@Override
	public Part resolveParent(Part child) {
//		IDataAccess api = ApiFactory.getDataAccessApi(issuedBy);
//		EntityInstance resolution = api.resolve(child.getID());
//		
//		return resolution == null ? null : (Part) resolution;
		
		// TODO The method must take into consideration a IView organizing the parts
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPartManager#getQualifiedName(org.hbird.exchange.interfaces.IPart)
	 */
	@Override
	public String getQualifiedName(Part part) {		
		return getQualifiedName(part, "/");
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IPartManager#getQualifiedName(org.hbird.exchange.interfaces.IPart, java.lang.String)
	 */
	@Override
	public String getQualifiedName(Part part, String separator) {
//		ICatalogue api = ApiFactory.getCatalogueApi(issuedBy);
//		List<Part> parts = api.getParts();
//		Map<String, Part> partMap = new HashMap<String, Part>();
//		for (Part aPart : parts) {
//			partMap.put(aPart.getID(), aPart);
//		}
		
		// TODO The method must take into consideration a IView organizing the parts
		return null;
	}
}
