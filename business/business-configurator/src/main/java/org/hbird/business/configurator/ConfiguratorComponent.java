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
package org.hbird.business.configurator;

import org.hbird.business.core.StartablePart;
import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;

/**
 * 
 * Component for starting other components.
 * 
 * @author Gert Villemos
 * 
 */
public class ConfiguratorComponent extends StartablePart {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2865072999875952015L;

    public static final String CONFIGURATOR_NAME = "Configurator";
    public static final String CONFIGURATOR_DESC = "A component for starting other components.";
	public static final String DEFAULT_DRIVER = ConfiguratorComponentDriver.class.getName();
	
	/**
	 * Default constructor.
	 */
	public ConfiguratorComponent() {
		super(CONFIGURATOR_NAME, CONFIGURATOR_DESC, DEFAULT_DRIVER);
	}

    {
        card.commandsIn.put(StartComponent.class.getName(), new StartComponent("", null));
        card.commandsIn.put(StopComponent.class.getName(), new StopComponent("", ""));
    }

    
    /**
     * Initialization method.
     * 
     * Typically called from the Spring XML file, using the init-method attribute.
     * 
     * @throws Exception
     */
    public void init() throws Exception {
        ConfiguratorComponentDriver driver = new ConfiguratorComponentDriver();
        driver.start(this);
    }
}
