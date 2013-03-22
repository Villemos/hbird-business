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

import org.hbird.business.core.InMemoryScheduler;
import org.hbird.exchange.interfaces.IStartablePart;

import eu.estcube.gs.base.HamlibDriver;
import eu.estcube.gs.base.Verifier;
import eu.estcube.gs.hamlib.HamlibDriverConfiguration;
import eu.estcube.gs.hamlib.HamlibIO;
import eu.estcube.gs.rotator.parameters.GetPosition;

/**
 * @author Admin
 *
 */
public class HamlibRotatorDriver extends HamlibDriver {

    /**
     * @param groundstationId
     * @param part
     * @param verifier
     * @param inMemoryScheduler-
     */
    public HamlibRotatorDriver(String groundstationId, IStartablePart part, Verifier verifier, InMemoryScheduler inMemoryScheduler) {
        super(groundstationId, part, verifier, inMemoryScheduler);
    }

    
    @Override
    public void doConfigure() {
        super.doConfigure();
        
        GetPosition getPosition = new GetPosition();
        
        /** Configure the monitoring routes. */
        from("timer://foo?period=3000")
            .bean(getPosition, "create")
            .inOut(HamlibIO.getDeviceDriverUrl((HamlibDriverConfiguration) config))
            .split().method(getPosition, "parse")
                .to("direct:parameters." + part.getQualifiedName("."));
            
    }    
}
