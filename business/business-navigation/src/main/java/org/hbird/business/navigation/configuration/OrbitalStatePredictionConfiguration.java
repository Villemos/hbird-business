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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class OrbitalStatePredictionConfiguration extends PredictionConfigurationBase {

    private static final long serialVersionUID = -9048419136572134995L;

    /** Default value for the prediction step. */
    public static final long DEFAULT_PREDICTION_STEP = 1000L * 60;

    @Value("${prediction.step:60000}")
    protected long predictionStep = DEFAULT_PREDICTION_STEP;

    /**
     * @return the predictionStep
     */
    public long getPredictionStep() {
        return predictionStep;
    }

    /**
     * @param predictionStep the predictionStep to set
     */
    public void setPredictionStep(long predictionStep) {
        this.predictionStep = predictionStep;
    }
}
