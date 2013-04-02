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
package org.hbird.business.archive;

import org.hbird.business.archive.solr.ArchiveComponentDriver;
import org.hbird.business.core.StartablePart;
import org.hbird.exchange.constants.StandardComponents;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.dataaccess.TleRequest;

/**
 * @author Gert Villemos
 *
 */
public class ArchiveComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145124224125316877L;

	public static final String DEFAULT_DRIVER = ArchiveComponentDriver.class.getName();
	
	/**
	 * @param name
	 * @param description
	 */
	public ArchiveComponent() {
		super(StandardComponents.ARCHIVE_NAME, StandardComponents.ARCHIVE_NAME, StandardComponents.ARCHIVE_DESC, DEFAULT_DRIVER);
	}

	{
		card.commandsIn.put(CommitRequest.class.getName(), new CommitRequest("", ""));
		card.commandsIn.put(DataRequest.class.getName(), new DataRequest("", ""));
		card.commandsIn.put(DeletionRequest.class.getName(), new DeletionRequest("", "", ""));
		card.commandsIn.put(GroundStationRequest.class.getName(), new GroundStationRequest("", ""));
		card.commandsIn.put(OrbitalStateRequest.class.getName(), new OrbitalStateRequest("", ""));
		card.commandsIn.put(ParameterRequest.class.getName(), new ParameterRequest("", "", 0));
		card.commandsIn.put(StateRequest.class.getName(), new StateRequest("", ""));
		card.commandsIn.put(TleRequest.class.getName(), new TleRequest("", ""));
    }
}
