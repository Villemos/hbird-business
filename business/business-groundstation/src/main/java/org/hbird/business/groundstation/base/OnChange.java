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
package org.hbird.business.groundstation.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Parameter;

/**
 * @author Admin
 * 
 */
// TODO - 28.04.2013, kimmell - make more generic; usable for all INamed objects
// TODO - 28.04.2013, kimmell - move to business core
public class OnChange {

    public static final int DEFAULT_ESTIMATED_NUMBER_OF_ELEMENTS = 128;

    protected final Map<String, Number> parameters;

    public OnChange() {
        this(DEFAULT_ESTIMATED_NUMBER_OF_ELEMENTS);
    }

    public OnChange(int estimatedNumberOfElements) {
        parameters = new ConcurrentHashMap<String, Number>(estimatedNumberOfElements);
    }

    public void process(@Body Parameter parameter, @Headers Map<String, Object> headers) {
        String name = parameter.getName();
        Number value = parameter.getValue();
        boolean hasChanged;

        if (parameters.containsKey(name)) {
            hasChanged = !parameters.get(name).equals(value);
        }
        else {
            parameters.put(name, value);
            hasChanged = true;
        }
        headers.put(StandardArguments.VALUE_HAS_CHANGED, hasChanged);
    }
}
