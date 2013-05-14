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

import org.apache.camel.TypeConverter;
import org.hbird.business.groundstation.configuration.GroundStationDriverConfiguration;
import org.hbird.business.groundstation.device.response.ResponseKeyExtractor;
import org.hbird.exchange.interfaces.IStartableEntity;

/**
 *
 */
public class DriverContext<C extends GroundStationDriverConfiguration, K, R> {

    protected final IStartableEntity part;
    protected final C configuration;
    protected final ResponseKeyExtractor<K, R> keyExtractor;
    protected final TypeConverter typeConverter;

    protected final Object deviceState;

    public DriverContext(IStartableEntity part, C configuration, ResponseKeyExtractor<K, R> keyExtractor, TypeConverter typeConverter,
            Object deviceState) {
        this.part = part;
        this.configuration = configuration;
        this.keyExtractor = keyExtractor;
        this.typeConverter = typeConverter;
        this.deviceState = deviceState;
    }

    /**
     * @return the configuration
     */
    public C getConfiguration() {
        return configuration;
    }

    /**
     * @return the deviceState
     */
    public Object getDeviceState() {
        return deviceState;
    }

    /**
     * @return the part
     */
    public IStartableEntity getPart() {
        return part;
    }

    /**
     * @return the keyExtractor
     */
    public ResponseKeyExtractor<K, R> getKeyExtractor() {
        return keyExtractor;
    }

    /**
     * @return the typeConverter
     */
    public TypeConverter getTypeConverter() {
        return typeConverter;
    }
}
