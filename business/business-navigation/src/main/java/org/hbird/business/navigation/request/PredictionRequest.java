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
package org.hbird.business.navigation.request;

import java.util.List;

import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.navigation.TleOrbitalParameters;

/**
 *
 */
public class PredictionRequest<T extends PredictionConfigurationBase> {

    /** Prediction configuration to use. */
    protected T configuration;

    /** Resolved TLE parameters. */
    protected TleOrbitalParameters tleParameters;

    /** Prediction start time. */
    protected long startTime;

    /** Prediction end time. */
    protected long endTime;

    /** List of prediction results. */
    protected List<? extends IEntityInstance> result;

    /**
     * @return the configuration
     */
    public T getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(T configuration) {
        this.configuration = configuration;
    }

    /**
     * @return the tleParameters
     */
    public TleOrbitalParameters getTleParameters() {
        return tleParameters;
    }

    /**
     * @param tleParameters the tleParameters to set
     */
    public void setTleParameters(TleOrbitalParameters tleParameters) {
        this.tleParameters = tleParameters;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the result
     */
    public List<? extends IEntityInstance> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<? extends IEntityInstance> result) {
        this.result = result;
    }
}
