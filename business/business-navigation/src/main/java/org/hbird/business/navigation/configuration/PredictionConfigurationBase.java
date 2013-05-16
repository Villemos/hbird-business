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
package org.hbird.business.navigation.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.core.ConfigurationBase;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 */
public abstract class PredictionConfigurationBase extends ConfigurationBase {

    private static final long serialVersionUID = 1000465272534384811L;

    /** Default value for the prediction period. */
    public static final int DEFAULT_PREDICTION_PERIOD = 24;

    /** Default value for the prediction interval. */
    public static final long DEFAULT_PREDICTION_INTERVAL = 1000L * 60 * 10;

    /** Satellite ID for the prediction. */
    @Value("${satellite.id}")
    protected String satelliteId;

    /** Prediction period in hours. */
    @Value("${prediction.period:24}")
    protected int predictionPeriod = DEFAULT_PREDICTION_PERIOD;

    /** Prediction execution interval in milliseconds. */
    @Value("${prediction.interval:60000}")
    protected long predictionInterval = DEFAULT_PREDICTION_INTERVAL;

    /**
     * @return the satelliteId
     */
    public String getSatelliteId() {
        return satelliteId;
    }

    /**
     * @param satelliteId the satelliteId to set
     */
    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    /**
     * Returns prediction period in hours.
     * 
     * @return the predictionPeriod
     */
    public int getPredictionPeriod() {
        return predictionPeriod;
    }

    /**
     * @param predictionPeriod the predictionPeriod to set
     */
    public void setPredictionPeriod(int predictionPeriod) {
        this.predictionPeriod = predictionPeriod;
    }

    /**
     * Returns prediction interval in milliseconds.
     * 
     * @return the predictionInterval
     */
    public long getPredictionInterval() {
        return predictionInterval;
    }

    /**
     * @param predictionInterval the predictionInterval to set
     */
    public void setPredictionInterval(long predictionInterval) {
        this.predictionInterval = predictionInterval;
    }

    /**
     * @see org.hbird.exchange.core.ConfigurationBase#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
