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
package org.hbird.exchange.util;

import java.util.Collection;

import org.hbird.exchange.core.EntityInstance;

/**
 * Helper methods for working with Named objects.
 */
public class NamedHelper {

    /**
     * Returns simple {@link String} representation of {@link Collection} of {@link EntityInstance} objects.
     * 
     * @param collection {@link Collection} of {@link EntityInstance}
     * @return simple {@link String} representation of {@link Collection} of {@link EntityInstance} objects
     */
    public static String toString(Collection<? extends EntityInstance> collection) {
        if (collection == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (EntityInstance named : collection) {
            sb.append(named.getName()).append(",");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
