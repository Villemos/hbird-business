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
package org.hbird.business.websockets;

import org.hbird.business.core.StartablePart;
import org.hbird.business.websockets.bean.WebsocketInterfaceComponentDriver;
import org.hbird.exchange.constants.StandardComponents;

/**
 * @author Gert Villemos
 *
 */
public class WebsocketInterfaceComponent extends StartablePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7161138730926653382L;

	public static final String DEFAULT_DRIVER = WebsocketInterfaceComponentDriver.class.getName();

	public WebsocketInterfaceComponent() {
		super(StandardComponents.WEB_SOCKET_NAME, StandardComponents.WEB_SOCKET_NAME, StandardComponents.WEB_SOCKET_DESC, DEFAULT_DRIVER);
	}
}
