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
package org.hbird.business.navigation.orekit;

import org.hbird.business.api.IPublisher;
import org.hbird.business.api.IdBuilder;
import org.hbird.business.api.IDataAccess;
import org.hbird.business.navigation.NavigationComponent;
import org.orekit.errors.OrekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gert Villemos
 * 
 */
public class OrbitPropagationBean extends NavigationBean {

    /**
     * @param configuration
     * @param dao
     * @param publisher
     * @param naming
     */
    public OrbitPropagationBean(NavigationComponent configuration, IDataAccess dao, IPublisher publisher, IdBuilder naming) {
        super(configuration, dao, publisher, naming);
    }

    protected static final Logger LOG = LoggerFactory.getLogger(OrbitPropagationBean.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.business.navigation.NavigationBean#prePropagation()
     */
    @Override
    public void preparePropagator() throws OrekitException {
    }
}
