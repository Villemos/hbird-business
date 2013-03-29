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
package org.hbird.business.archive;

import org.hbird.business.archive.solr.ArchiveComponentDriver;
import org.hbird.exchange.configurator.StartablePart;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.dataaccess.TleRequest;

/**
 * @author Admin
 *
 */
public class ArchiveComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145124224125316877L;

	public static final String DEFAULT_NAME = "Archive";
	public static final String DEFAULT_DESCRIPTION = "The archive of data";
	public static final String DEFAULT_DRIVER = ArchiveComponentDriver.class.getName();
	
	/**
	 * @param name
	 * @param description
	 */
	public ArchiveComponent() {
		super(DEFAULT_NAME, DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DRIVER);
	}

	{
        commandsIn.add(new CommitRequest("", ""));
        commandsIn.add(new DataRequest("", ""));
        commandsIn.add(new DeletionRequest("", "", ""));
        commandsIn.add(new GroundStationRequest("", ""));
        commandsIn.add(new OrbitalStateRequest(""));
        commandsIn.add(new ParameterRequest("", 0));
        commandsIn.add(new StateRequest("", ""));
        commandsIn.add(new TleRequest("", ""));
    }
}
