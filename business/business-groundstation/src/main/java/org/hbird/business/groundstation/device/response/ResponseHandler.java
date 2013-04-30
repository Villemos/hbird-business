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
package org.hbird.business.groundstation.device.response;

import java.util.List;

import org.hbird.business.groundstation.base.DriverContext;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.exchange.interfaces.IEntityInstance;

/**
 *
 */
public interface ResponseHandler<C extends GroundStationDriverConfiguration, K, R> {

    public K getKey();

    public List<IEntityInstance> handle(DriverContext<C, K, R> driverContext, R response);
}
