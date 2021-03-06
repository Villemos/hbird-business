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
package org.hbird.business.api;

import org.apache.camel.Handler;
import org.hbird.exchange.core.EntityInstance;

/**
 * API Interface for publishing data to the system.
 * 
 * The API should be used by any element which needs to publish data that shall be distributed through
 * the Hummingbird system. The API will publish the {@link EntityInstance} it to the underlying
 * protocol, typically being activemq. The further distribution of the object depends on the
 * assembly of the system.
 * 
 * @author Gert Villemos
 * 
 */
public interface IPublisher {

    @Handler
    public <T extends EntityInstance> T publish(T object) throws Exception;
}
