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
package eu.estcube.gs.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.hbird.exchange.core.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Gert Villemos
 *
 */
public class Verifier {

	private static final Logger LOG = LoggerFactory.getLogger(Verifier.class);

	/** Map of the stages of verification for the Track command. 
	 * 
	 * The map is keyed on the Command ID. The value is keyed on Stage name and the value is the set of Native commands
	 * that are part of this stage.
	 */
	protected Map<String, Map<String, List<String>>> verificationStages = new HashMap<String, Map<String, List<String>>>();

	protected Map<String, Long> cleanup = new HashMap<String, Long>();

	protected long gracePeriod = 10000;

	protected Map<String, String> stageDescription = new HashMap<String, String>();
	{
		stageDescription.put("PreTracking", "The verification of the preparation of a track.");
		stageDescription.put("Tracking", "The verification of the track.");
		stageDescription.put("PostTracking", "The verification of the cleanup after a track.");
	}

	public synchronized void register(@Header("stage") String stage, @Header("derivedfrom") String derivedFrom, @Header("commandid") String commandid, @Header("executiontime") Long executiontime) {                
		LOG.debug("Stage '" + stage + "' depends on nativecommand '" + commandid + "'.");

		if (verificationStages.containsKey(derivedFrom) == false) {
			verificationStages.put(derivedFrom, new HashMap<String, List<String>>());
		}

		if (verificationStages.get(derivedFrom).containsKey(stage) == false) {
			verificationStages.get(derivedFrom).put(stage, new ArrayList<String>());
		}

		verificationStages.get(derivedFrom).get(stage).add(commandid);

		if (cleanup.containsKey(derivedFrom) == false) {
			cleanup.put(derivedFrom, executiontime);
		}
		else if (cleanup.get(derivedFrom) < executiontime) {
			cleanup.put(derivedFrom, executiontime);
		}
	}

	public synchronized State verify(@Body String response, @Header("stage") String stage, @Header("derivedfrom") String derivedFrom, @Header("commandid") String commandid) {
		State state = null;

		/** A positive hamlib response contains 'RPRT 0' as the return code. 
		 *  Any other value indicates and error. */
		if (verificationStages.containsKey(derivedFrom)) {

			if (response.contains("RPRT 0") == false) {
				LOG.error("Native command '" + commandid + "' failed execution; '" + response.replaceAll("\n", "") + "' as part of stage '" + stage + "'. Removing verification of command " + derivedFrom);

				/** Command has failed. We dont register the remaining responses. */
				verificationStages.remove(derivedFrom);

				state = createState(stage, false, derivedFrom);
			}
			else {
				/** Remove from the stages */
				LOG.debug("Native command '" + commandid + "' succeeded; '" + response.replaceAll("\n", "") + ". Removing commandid '" + commandid + "' from stage '" + stage + "' of command '" + derivedFrom + "'.");

				verificationStages.get(derivedFrom).get(stage).remove(commandid);

				/** If this stage is finished, then remove it. */
				if (verificationStages.get(derivedFrom).get(stage).isEmpty()) {
					LOG.info("Verification stage '" + stage + "' completed successfully for command '" + derivedFrom + "'.");
					state = createState(stage, true, derivedFrom);
					verificationStages.get(derivedFrom).remove(stage);

					/** If all stages have finished, then remove this parent. */
					if (verificationStages.get(derivedFrom).isEmpty()) {
						LOG.info("Verification completed succesfully for command '" + derivedFrom + "'");
						verificationStages.remove(derivedFrom);
					}
				}
			}
		}
		else {
			LOG.warn("No verification stages found for nativecommand '" + commandid + "' of command '" + derivedFrom + "'. Response was '" + response + "'.");
		}
		
		return state;
	}

	public synchronized List<State> cleanup() {
		List<State> states = new ArrayList<State>();

		long now = System.currentTimeMillis();

		Iterator<Entry<String, Long>> it = cleanup.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Long> entry = it.next();

			if (entry.getValue() + gracePeriod < now) {

				/** If the command is still in the verification queue, then something went wrong. */
				if (verificationStages.containsKey(entry.getKey())) {

					LOG.error("Verification of command '" + entry.getKey() + "' is not completed.");

					for (String stage : verificationStages.get(entry.getKey()).keySet()) {
						states.add(createState(stage, false, entry.getKey()));
					}
				}

				verificationStages.remove(entry.getKey());
				it.remove();
			}
		}

		return states;
	}

	protected State createState(String stage, boolean state, String derivedFrom) {
		LOG.info("Sending state '" + state + "' for stage '" + stage + "' of command '" + derivedFrom + "'");
		return new State("Rotator", stage, "State", stageDescription.get(stage), derivedFrom, state);
	}
}
