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
package eu.estcube.gs.rotator;

import eu.estcube.gs.base.HamlibDriver;
import eu.estcube.gs.configuration.RotatorDriverConfiguration;
import eu.estcube.gs.rotator.parameters.GetPosition;

/**
 * @author Admin
 * 
 */
public class HamlibRotatorDriver extends HamlibDriver {

    @Override
    public void doConfigure() {
        super.doConfigure();

        GetPosition getPosition = new GetPosition();

        /** Configure the monitoring routes. */
        from("timer://foo?period=" + getDriverConfiguration().getDevicePollInterval())
                .bean(getPosition, "create")
                .inOut("netty:tcp://" + getAddress())
                .split().method(getPosition, "parse")
                .to("direct:parameters." + part.getName());

    }

    /*
     * (non-Javadoc)
     * 
     * @see eu.estcube.gs.base.HamlibDriver#getAddress()
     */
    @Override
    public String getAddress() {
        return getDriverConfiguration().getAddress();
    }

    RotatorDriverConfiguration getDriverConfiguration() {
        HamlibRotatorPart rotator = (HamlibRotatorPart) part;
        return rotator.getConfiguration();
    }
}
