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

import org.hbird.business.api.HbirdApi;
import org.hbird.business.api.IArchiveManagement;
import org.hbird.business.archive.ArchiveComponent;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.navigation.OrbitalState;

/**
 * API for management of the archive component. Support
 * <li>Deletion of Data.</li>
 * 
 * @author Gert Villemos
 *
 */
public class ArchiveManagement extends HbirdApi implements IArchiveManagement {

	/**
	 * Constructor. 
	 * 
	 * @param issuedBy The name/ID of the component that is using the API to send requests.
	 */
	public ArchiveManagement(String issuedBy) {
		super(issuedBy, ArchiveComponent.ARCHIVE_NAME);
	}

	/* (non-Javadoc)
	 * @see org.hbird.business.api.IArchiveManagement#deleteOrbitalStates(java.lang.String)
	 */
	@Override
	public void deleteOrbitalStates(String satellite) {
		DeletionRequest request = new DeletionRequest(issuedBy);
		request.setArgumentValue(StandardArguments.SATELLITE_NAME, satellite);
		request.setArgumentValue(StandardArguments.CLASS, OrbitalState.class.getSimpleName());
		executeRequestRespond(request);
	}
}
