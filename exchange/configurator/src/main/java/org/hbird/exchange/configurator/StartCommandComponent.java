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
package org.hbird.exchange.configurator;

import org.hbird.exchange.constants.StandardComponents;

public class StartCommandComponent extends StartComponent {

    public static final String DESCRIPTION = "Command to a configurator to start a command component.";

    private static final long serialVersionUID = -6699015161668631249L;

    public StartCommandComponent() {
        this(StandardComponents.COMMANDING_CHAIN);
    }

    public StartCommandComponent(String componentname) {
        super(componentname, StartCommandComponent.class.getSimpleName(), DESCRIPTION);
    }

    public StartCommandComponent(String issuedBy, String destination, String componentname) {
        super(issuedBy, destination, componentname, "StartCommandComponent", DESCRIPTION);
    }
}
