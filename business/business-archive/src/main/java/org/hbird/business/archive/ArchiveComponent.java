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

import java.util.List;

import org.hbird.business.archive.solr.ArchiveComponentDriver;
import org.hbird.business.core.StartablePart;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.dataaccess.TleRequest;

/**
 * StartablePart for storage of data.
 * 
 * @author Gert Villemos
 */
public class ArchiveComponent extends StartablePart {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2145124224125316877L;

    public static final String ARCHIVE_NAME = "Archive";

    public static final String ARCHIVE_DESC = "The archive of data";

    /** Default driver name (SOLR) */
    public static final String DEFAULT_DRIVER = ArchiveComponentDriver.class.getName();

    /**
     * Default constructor.
     */
    public ArchiveComponent() {
        super(ARCHIVE_NAME, ARCHIVE_DESC, DEFAULT_DRIVER);
    }

    /**
     * @see org.hbird.business.core.StartablePart#createCommandList()
     */
    @Override
    protected List<Command> createCommandList(List<Command> commands) {
        commands.add(new CommitRequest(""));
        commands.add(new DataRequest(""));
        commands.add(new DeletionRequest(""));
        commands.add(new GroundStationRequest(""));
        commands.add(new OrbitalStateRequest("", ""));
        commands.add(new ParameterRequest("", "", 0));
        commands.add(new StateRequest("", ""));
        commands.add(new TleRequest("", ""));
        return commands;
    }
}
