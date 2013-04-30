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
package org.hbird.exchange.groundstation;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.CommandArgument;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;

/**
 * Command to track a satellite from a start event to a stop event.
 * 
 * @author Gert Villemos
 */
public class Track extends Command {

    private static final long serialVersionUID = -5047301867698410904L;

    public static final String DESCRIPTION = "Command to track a satellite during the location contact event.";

    /**
     * @param issuedBy
     * @param destination
     */
    public Track(String issuedBy, String destination, Satellite satellite, LocationContactEvent event) {
        super(issuedBy, destination, Track.class.getSimpleName(), DESCRIPTION);
        setArgumentValue(StandardArguments.CONTACT, event);
        setArgumentValue(StandardArguments.SATELLITE, satellite);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args.add(new CommandArgument(StandardArguments.CONTACT, "The contact to track", LocationContactEvent.class, "", null, true));
        args.add(new CommandArgument(StandardArguments.SATELLITE, "The satellite", Satellite.class, "", null, true));
        return args;
    }

    public LocationContactEvent getLocationContactEvent() {
        return getArgumentValue(StandardArguments.CONTACT, LocationContactEvent.class);
    }

    public Satellite getSatellite() {
        return getArgumentValue(StandardArguments.SATELLITE, Satellite.class);
    }
}
