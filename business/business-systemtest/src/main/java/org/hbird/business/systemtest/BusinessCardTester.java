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
package org.hbird.business.systemtest;

import org.apache.camel.Handler;
import org.apache.log4j.Logger;
import org.hbird.business.commanding.CommandingComponent;
import org.hbird.business.configurator.ConfiguratorComponent;
import org.hbird.exchange.configurator.ReportStatus;
import org.hbird.exchange.configurator.StandardEndpoints;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.interfaces.IStartablePart;

public class BusinessCardTester extends SystemTest {

	private static org.apache.log4j.Logger LOG = Logger.getLogger(BusinessCardTester.class);

	@Handler
	public void process() throws InterruptedException {

		LOG.info("------------------------------------------------------------------------------------------------------------");
		LOG.info("Starting");

		/** See if we have the businesscards of the Configurator. */
		Thread.sleep(8000);

		synchronized (businessCardListener.elements) {
			Boolean didArrive = false;
			for (Named obj : businessCardListener.elements) {
				LOG.info(obj);
				if (obj instanceof Named) {
					/** Notice that the name given to the configurator in the assembly is 'Main Configurator' */
					if (((Named)obj).getIssuedBy().equals(ConfiguratorComponent.CONFIGURATOR_NAME)) {
						didArrive = true;
						break;
					}
				}
			}

			azzert(didArrive, "Business card messages arrive from Configurator");
			businessCardListener.elements.clear();
		}

		startCommandingChain();

		Thread.sleep(3000);

		IStartablePart commanding = (IStartablePart) parts.get(CommandingComponent.COMMAND_RELEASER_NAME);

		synchronized (businessCardListener.elements) {
			Boolean commandingDidArrive = false;
			for (Named obj : businessCardListener.elements) {
				LOG.info(obj);
				if (obj instanceof Named) {

					if (((Named)obj).getIssuedBy().equals(CommandingComponent.COMMAND_RELEASER_NAME)) {
						commandingDidArrive = true;
						break;
					}
				}
			}

			azzert(commandingDidArrive, "Business card messages arrive from CommandingChain");
			businessCardListener.elements.clear();
		}

		Thread.sleep(3000);

		Object data = injection.requestBody(new ReportStatus("SystemTest", "Configurator"));
		azzert(data != null, "Status received from Configurator.");

		/** Stop the CommandingChain and check that it is actually stopped. */
		stopCommandingChain();

		businessCardListener.elements.clear();

		Thread.sleep(3000);

		synchronized (businessCardListener.elements) {
			Boolean commandingDidArrive = false;
			for (Named obj : businessCardListener.elements) {
				LOG.info(obj);
				if (obj instanceof Named) {

					if (((Named)obj).getIssuedBy().equals(commanding.getName())) {
						commandingDidArrive = true;
						break;
					}
				}
			}

			azzert(!commandingDidArrive, "Business card messages not arriving from CommandingChain");
		}

		LOG.info("Finished");
	}
}
