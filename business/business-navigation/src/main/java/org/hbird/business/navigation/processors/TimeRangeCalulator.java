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
package org.hbird.business.navigation.processors;

import org.apache.camel.Handler;
import org.hbird.business.navigation.configuration.PredictionConfigurationBase;
import org.hbird.business.navigation.request.PredictionRequest;
import org.hbird.exchange.util.Dates;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TimeRangeCalulator {

    private static final Logger LOG = LoggerFactory.getLogger(TimeRangeCalulator.class);

    @Handler
    public PredictionRequest<?> calculate(PredictionRequest<?> request) {

        PredictionConfigurationBase config = request.getConfiguration();
        int period = config.getPredictionPeriod();

        long startTime = request.getStartTime();
        DateTime start = new DateTime(startTime, DateTimeZone.UTC).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime end = start.plusHours(period);
        LOG.info("Prediction period {}h; from {} to {}",
                new Object[] { period, start.toString(Dates.ISO_8601_DATE_FORMATTER), end.toString(Dates.ISO_8601_DATE_FORMATTER) });

        request.setStartTime(start.getMillis());
        request.setEndTime(end.getMillis());
        return request;
    }
}
