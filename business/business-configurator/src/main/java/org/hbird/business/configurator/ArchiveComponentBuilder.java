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
package org.hbird.business.configurator;

import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.dataaccess.GroundStationRequest;
import org.hbird.exchange.dataaccess.OrbitalStateRequest;
import org.hbird.exchange.dataaccess.ParameterRequest;
import org.hbird.exchange.dataaccess.StateRequest;
import org.hbird.exchange.dataaccess.TleRequest;

/**
 * Component builder to create an archive component.
 * 
 * @author Gert Villemos
 */
public class ArchiveComponentBuilder extends ComponentBuilder {

    {
        commands.add(new CommitRequest("", ""));
        commands.add(new DataRequest("", ""));
        commands.add(new DeletionRequest("", "", ""));
        commands.add(new GroundStationRequest("", ""));
        commands.add(new OrbitalStateRequest(""));
        commands.add(new ParameterRequest("", 0));
        commands.add(new StateRequest("", ""));
        commands.add(new TleRequest("", ""));
    }

    @Override
    protected void doConfigure() {

        // + "selector=type!='BusinessCard'"
        from(StandardEndpoints.monitoring).to("solr:" + getComponentName());
        from(StandardEndpoints.commands).to("solr:" + getComponentName());
    };
}
