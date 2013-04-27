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
package org.hbird.business.groundstation.hamlib.protocol;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class HamlibProtocolHelper {

    /**
     * Checks if response last line starts with {@link HamlibProtocolConstants#DEVICE_END_MESSAGE}.<br/>
     * 
     * @param response
     * @return
     */
    public static boolean isFullMessge(String response) {
        return getLastLine(response).startsWith(HamlibProtocolConstants.DEVICE_END_MESSAGE);
    }

    /**
     * Returns last line from multi line string.
     * 
     * @param response
     * @return
     */
    public static String getLastLine(String response) {
        String[] lines = response.split("\n");
        return lines[lines.length - 1];
    }

    /**
     * Returns error code from Hamlib response.<br/>
     * 
     * In case the response last line is not starting with the {@link HamlibProtocolConstants#DEVICE_END_MESSAGE} return
     * <tt>null</tt>.
     * 
     * Error code <tt>0</tt> marks successful response. Other values indicate error in the Hamlib.
     * 
     * @param response response string from Hamlib
     * @return Hamlib response error code or <tt>null</tt>
     */
    public static String getErrorCode(String response) {
        String lastLine = getLastLine(response);
        if (lastLine.startsWith(HamlibProtocolConstants.DEVICE_END_MESSAGE)) {
            return lastLine.substring(HamlibProtocolConstants.DEVICE_END_MESSAGE.length() + 1);
        }
        return null;
    }

    /**
     * Checks if Hamlib response is error message.
     * 
     * @param response
     * @return
     */
    public static boolean isErrorResponse(String response) {
        return !"0".equals(getErrorCode(response));
    }

    public static Map<String, String> toMap(String response) {
        String[] lines = StringUtils.split(response, "\n");

        Map<String, String> map = new HashMap<String, String>();
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                // skip the blank lines
                continue;
            }
            String key;
            String value;
            if (line.startsWith(HamlibProtocolConstants.DEVICE_END_MESSAGE)) {
                key = HamlibProtocolConstants.DEVICE_END_MESSAGE;
                value = StringUtils.substringAfter(line, HamlibProtocolConstants.DEVICE_END_MESSAGE);
            }
            else {
                key = StringUtils.substringBefore(line, ":");
                value = StringUtils.substringAfter(line, ":");
            }
            map.put(key.trim(), value.trim());
        }
        return map;
    }
}
