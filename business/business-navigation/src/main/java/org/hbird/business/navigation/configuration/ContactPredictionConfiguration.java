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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ContactPredictionConfiguration extends PredictionConfigurationBase {

    private static final long serialVersionUID = 3783672950051045639L;

    /** Default value for calculation step. */
    public static final long DEFAULT_DETAILS_CALCULATION_STEP = 1000L * 30;

    /** Calculation step used for contact detail calculations. For example used to calculate maximum elevation etc. */
    @Value("${details.calculation.step}")
    protected long detailsCalculationStep = DEFAULT_DETAILS_CALCULATION_STEP;

    /** List of ground stations to use in prediction. */
    protected List<String> groundStationsIds = new ArrayList<String>();

    /**
     * @return the detailsCalculationStep
     */
    public long getDetailsCalculationStep() {
        return detailsCalculationStep;
    }

    /**
     * @param detailsCalculationStep the detailsCalculationStep to set
     */
    public void setDetailsCalculationStep(long detailsCalculationStep) {
        this.detailsCalculationStep = detailsCalculationStep;
    }

    /**
     * @return the groundStationsIds
     */
    public List<String> getGroundStationsIds() {
        return groundStationsIds;
    }

    /**
     * @param groundStationsIds the groundStationsIds to set
     */
    public void setGroundStationsIds(List<String> groundStationsIds) {
        this.groundStationsIds = groundStationsIds;
    }
}
