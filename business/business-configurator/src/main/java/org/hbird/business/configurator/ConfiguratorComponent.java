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

import org.hbird.exchange.configurator.StartComponent;
import org.hbird.exchange.configurator.StopComponent;
import org.hbird.exchange.core.StartablePart;


/**
 * @author Admin
 *
 */
public class ConfiguratorComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2865072999875952015L;

	{
		commands.add(new StartComponent("", null));
		commands.add(new StopComponent("", ""));
	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfiguratorComponent(String name, String driver) {
		super(name, "A component for starting other component.", driver);
	}

	/**
	 */
	public ConfiguratorComponent() {
		this("Configurator", ConfiguratorComponentDriver.class.getName());
	}

	public void init() {
		new ConfiguratorComponentDriver();
	}
}
