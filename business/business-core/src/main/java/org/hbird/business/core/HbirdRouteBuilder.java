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
package org.hbird.business.core;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;

/**
 * An extension of the RouteBuilder, supporting standard injection routes.
 * 
 * @author Gert Villemos
 * 
 */
public abstract class HbirdRouteBuilder extends RouteBuilder {

    /**
     * Adds to a route the injection path into hummingbird. The injection path can
     * be complex. It will set headers for routing and filtering purposes. It will
     * route different kinds of messages to different endpoints for distribution.
     * 
     * @param route
     */
    protected void addInjectionRoute(ProcessorDefinition<?> route) {
        AddHeaders addHeaders = new AddHeaders();
        EntityRouter router = new EntityRouter();

        // @formatter:off
        route
            .process(addHeaders)
            .process(router)
            .recipientList(header(EntityRouter.ROUTING_HEADER))
            .end();
        // @formatter:on
    }
}
