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
package org.hbird.business.groundstation.hamlib.rotator;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.configuration.RotatorDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.device.response.ResponseKeyExtractor;
import org.hbird.business.groundstation.hamlib.HamlibDriver;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseKeyExtractor;
import org.hbird.business.groundstation.hamlib.rotator.protocol.GetPosition;
import org.hbird.business.groundstation.hamlib.rotator.protocol.Park;
import org.hbird.business.groundstation.hamlib.rotator.protocol.Reset;
import org.hbird.business.groundstation.hamlib.rotator.protocol.SetPosition;
import org.hbird.business.navigation.orekit.PointingDataCalculator;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.interfaces.IStartableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Admin
 * 
 */
public class HamlibRotatorDriver extends HamlibDriver<RotatorDriverConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(HamlibRotatorDriver.class);

    @Override
    public void doConfigure() {
        super.doConfigure();

        GroundStationDriverConfiguration config = driverContext.getConfiguration();

        LOG.debug("Setting up hamlib rotator position poll using timeout {} ms and address '{}'", config.getDevicePollInterval(), config.getAddress());

        long interval = config.getDevicePollInterval();
        String name = part.getName();

        /** Configure the monitoring routes. */
        // @formatter:off
        from(asRoute("timer://HamlibPollTimer?period=%s&delay=%s", interval, interval)) // using shared timer here
            .setBody(constant(GetPosition.COMMAND))
            .to(asRoute("seda:toHamlib-%s", name));
        // @formatter:on

    }

    /**
     * @see org.hbird.business.groundstation.base.HamlibDriver#createResponseHandlers()
     *      -
     */
    @Override
    protected List<ResponseHandler<RotatorDriverConfiguration, String, String>> createResponseHandlers() {
        List<ResponseHandler<RotatorDriverConfiguration, String, String>> list = new ArrayList<ResponseHandler<RotatorDriverConfiguration, String, String>>(4);
        list.add(new GetPosition());
        list.add(new Park());
        list.add(new Reset());
        list.add(new SetPosition());
        return list;
    }

    /**
     * @see org.hbird.business.groundstation.hamlib.HamlibDriver#createDriverContext(org.hbird.exchange.interfaces.IPart)
     */
    @Override
    protected DriverContext<RotatorDriverConfiguration, String, String> createDriverContext(CamelContext camelContext, IStartableEntity part) {
        HamlibRotatorPart rotator = (HamlibRotatorPart) part;
        ResponseKeyExtractor<String, String> keyExtractor = new HamlibResponseKeyExtractor();
        RotatorState deviceState = new RotatorState();
        DriverContext<RotatorDriverConfiguration, String, String> context = new DriverContext<RotatorDriverConfiguration, String, String>(part,
                rotator.getConfiguration(), keyExtractor, camelContext.getTypeConverter(), deviceState);
        return context;
    }

    /**
     * @see org.hbird.business.groundstation.hamlib.HamlibDriver#createTrackingSupport(org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration,
     *      org.hbird.business.api.ICatalogue, org.hbird.business.api.IOrbitPrediction,
     *      org.hbird.exchange.groundstation.IPointingDataOptimizer)
     */
    @Override
    protected TrackingSupport<RotatorDriverConfiguration> createTrackingSupport(RotatorDriverConfiguration config, ICatalogue catalogue,
            PointingDataCalculator calculator, IPointingDataOptimizer<RotatorDriverConfiguration> optimizer) {
        return new HamlibRotatorTracker(config, catalogue, calculator, optimizer);
    }
}
