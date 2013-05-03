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
package org.hbird.business.groundstation.hamlib.radio;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.hbird.business.api.ICatalogue;
import org.hbird.business.api.IOrbitPrediction;
import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.base.TrackingSupport;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.configuration.RadioDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseHandler;
import org.hbird.business.groundstation.device.response.ResponseKeyExtractor;
import org.hbird.business.groundstation.hamlib.HamlibDriver;
import org.hbird.business.groundstation.hamlib.protocol.HamlibResponseKeyExtractor;
import org.hbird.business.groundstation.hamlib.radio.protocol.GetFrequency;
import org.hbird.business.groundstation.hamlib.radio.protocol.SetFrequency;
import org.hbird.business.groundstation.hamlib.radio.protocol.SetVfo;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.interfaces.IStartablePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Admin
 * 
 */
public class HamlibRadioDriver extends HamlibDriver<RadioDriverConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(HamlibRadioDriver.class);

    @Override
    public void doConfigure() {
        super.doConfigure();

        GroundStationDriverConfiguration config = driverContext.getConfiguration();

        LOG.debug("Setting up Hamlib radio frequency poll using timeout {} ms and address '{}'", config.getDevicePollInterval(), config.getAddress());

        long interval = config.getDevicePollInterval();
        String name = part.getName();

        /** Configure the monitoring routes. */
        // @formatter:off
        
        from(asRoute("timer://HamlibPollTimer?period=%s&delay=%s", interval, interval)) // using shared timer here
            .setBody(constant(GetFrequency.COMMAND))
            .to(asRoute("seda:toHamlib-%s", name));
        
        // @formatter:on

    }

    /**
     * @see org.hbird.business.groundstation.hamlib.HamlibDriver#createResponseHandlers()
     */
    @Override
    protected List<ResponseHandler<RadioDriverConfiguration, String, String>> createResponseHandlers() {
        List<ResponseHandler<RadioDriverConfiguration, String, String>> list = new ArrayList<ResponseHandler<RadioDriverConfiguration, String, String>>(3);
        list.add(new SetFrequency());
        list.add(new GetFrequency());
        list.add(new SetVfo());
        return list;
    }

    /**
     * @see org.hbird.business.groundstation.hamlib.HamlibDriver#createDriverContext(org.hbird.exchange.interfaces.IPart)
     */
    @Override
    protected DriverContext<RadioDriverConfiguration, String, String> createDriverContext(CamelContext camelContext, IStartablePart part) {
        HamlibRadioPart radio = (HamlibRadioPart) part;
        RadioDriverConfiguration config = radio.getConfiguration();
        ResponseKeyExtractor<String, String> keyExtractor = new HamlibResponseKeyExtractor();
        RadioState deviceState = new RadioState();
        DriverContext<RadioDriverConfiguration, String, String> context = new DriverContext<RadioDriverConfiguration, String, String>(part, config, keyExtractor, camelContext.getTypeConverter(), deviceState);
        return context;
    }

    /**
     * @see org.hbird.business.groundstation.hamlib.HamlibDriver#createTrackingSupport(org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration,
     *      org.hbird.business.api.ICatalogue, org.hbird.business.api.IOrbitPrediction,
     *      org.hbird.exchange.groundstation.IPointingDataOptimizer)
     */
    @Override
    protected TrackingSupport<RadioDriverConfiguration> createTrackingSupport(RadioDriverConfiguration config, ICatalogue catalogue,
            IOrbitPrediction prediction, IPointingDataOptimizer<RadioDriverConfiguration> optimizer) {
        return new HamlibRadioTracker(config, catalogue, prediction, optimizer);
    }
}
