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
package org.hbird.business.groundstation.base;

import java.util.List;

import org.hbird.exchange.groundstation.GroundStationConfigurationBase;
import org.hbird.exchange.groundstation.IPointingDataOptimizer;
import org.hbird.exchange.navigation.PointingData;

/**
 * Default dummy implementation for the {@link IPointingDataOptimizer}.
 */
public class DefaultPointingDataOptimizer<C extends GroundStationConfigurationBase> implements IPointingDataOptimizer<C> {

    /**
     * @see org.hbird.exchange.groundstation.IPointingDataOptimizer#optimize(java.util.List,
     *      org.hbird.exchange.groundstation.GroundStationConfigurationBase)
     */
    @Override
    public List<PointingData> optimize(List<PointingData> pointingData, C configuration) throws Exception {
        return pointingData;
    }
}
