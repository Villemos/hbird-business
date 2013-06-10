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
package org.hbird.business.navigation.processors.orekit;

import org.apache.camel.Handler;
import org.hbird.business.navigation.configuration.ContactPredictionConfiguration;
import org.hbird.business.navigation.request.ContactPredictionRequest;
import org.hbird.business.navigation.request.orekit.ContactData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class ContactPredictionRequestCreator {

    public static final Logger LOG = LoggerFactory.getLogger(ContactPredictionRequestCreator.class);

    @Autowired
    private final ContactPredictionConfiguration config;

    public ContactPredictionRequestCreator(ContactPredictionConfiguration config) {
        this.config = config;
    }

    @Handler
    public ContactPredictionRequest<ContactData> create() {
        ContactPredictionRequest<ContactData> request = new ContactPredictionRequest<ContactData>(System.currentTimeMillis());
        request.setConfiguration(config);
        LOG.debug("Created new ContactPredictionRequest");
        return request;
    }
}
