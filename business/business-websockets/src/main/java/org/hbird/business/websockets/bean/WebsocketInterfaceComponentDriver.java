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
package org.hbird.business.websockets.bean;

import org.apache.camel.Endpoint;
import org.apache.camel.component.websocket.WebsocketComponent;
import org.apache.camel.component.websocket.WebsocketConstants;
import org.hbird.business.core.SoftwareComponentDriver;
import org.hbird.business.websockets.WebsocketInterfaceComponent;

/**
 * @author Lauri Kimmel
 * @author Gert Villemos (adaptation)
 *
 */
public class WebsocketInterfaceComponentDriver extends SoftwareComponentDriver<WebsocketInterfaceComponent> {

	protected int webSocketPort = 9292;

	protected String staticResources = "file:/hbird-webui/org.hbird.webui/src/public";

	protected ToJsonProcessor toJson = new ToJsonProcessor();

	/* (non-Javadoc)
	 * @see org.hbird.business.configurator.ComponentBuilder#doConfigure()
	 */
	@Override
	protected void doConfigure() {

		WebsocketComponent websocketComponent = (WebsocketComponent) getContext().getComponent("websocket");

		websocketComponent.setPort(webSocketPort);
		websocketComponent.setStaticResources(staticResources);

		Endpoint webSocketForParameters = null;
		Endpoint webSocketForLog = null;
		Endpoint webSocketForEvents = null;
		Endpoint webSocketForStates = null;
		
		try {
			webSocketForParameters = websocketComponent.createEndpoint("websocket://localhost:" + webSocketPort + "/parameters");
			webSocketForLog = websocketComponent.createEndpoint("websocket://localhost:" + webSocketPort + "/logs");
			webSocketForEvents = websocketComponent.createEndpoint("websocket://localhost:" + webSocketPort + "/events");
			webSocketForStates = websocketComponent.createEndpoint("websocket://localhost:" + webSocketPort + "/states");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// @formatter:off

		from("activemq:topic:hbird.monitoring?selector=class='State'")
		.bean(toJson)
		.setHeader(WebsocketConstants.SEND_TO_ALL, constant(true))
		.to(webSocketForStates);

		from("activemq:topic:hbird.monitoring?selector=class='Event'")
		.bean(toJson)
		.setHeader(WebsocketConstants.SEND_TO_ALL, constant(true))
		.to(webSocketForEvents);

		from("activemq:topic:hbird.monitoring?selector=class='Parameter'")
		.bean(toJson)
		.setHeader(WebsocketConstants.SEND_TO_ALL, constant(true))
		.to(webSocketForParameters);

		from("activemq:topic:systemlog")
		.bean(toJson)
		.setHeader(WebsocketConstants.SEND_TO_ALL, constant(true))
		.to(webSocketForLog);
	}
}
