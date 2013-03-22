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
package eu.estcube.gs.rotator.parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hbird.exchange.core.Parameter;

/**
 * @author Admin
 *
 */
public class GetPosition {

    protected String issuedBy;

    protected Pattern pattern = Pattern.compile("get_pos:\\nAzimuth: (.*?)\\nElevation: (.*?)\nRPRT 0", Pattern.MULTILINE);

    public String create() {
        return "+p\n";
    }

    public List<Parameter> parse(String respond) {
        List<Parameter> parameters = new ArrayList<Parameter>();

        if (respond != null) {
            Matcher matcher = pattern.matcher(respond);

            if (matcher.find()) {
                parameters.add(new Parameter(issuedBy, issuedBy + "/azimuth", "Parameter", "The azimuth of the rotator.", Double.parseDouble(matcher.group(1)), "Degree"));
                parameters.add(new Parameter(issuedBy, issuedBy + "/elevation", "Parameter", "The elevation of the rotator.", Double.parseDouble(matcher.group(2)), "Degree"));
            }
        }
        return parameters;
    }
}
