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
package org.hbird.exchange.interfaces;

import org.apache.camel.CamelContext;
import org.hbird.exchange.core.BusinessCard;

/**
 * @author Gert Villemos
 * 
 */
public interface IStartableEntity extends IEntityInstance {

    public String getDriverName();

    public void setDriverName(String driverName);

    public long getHeartbeat();

    public void setHeartbeat(long heartbeat);

    /**
     * Returns {@link BusinessCard} describing the {@link IStartableEntity}.
     * 
     * @return {@link BusinessCard} describing the {@link IStartableEntity}
     */
    public BusinessCard getBusinessCard();

    public void setContext(CamelContext context);

    public CamelContext getContext();
}
