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
package org.hbird.business.api;

/**
 * Interface to provide ID building functionality.
 * For example can be used to build dynamic parameter ID if applicable Entity ID and parameter name are known.
 * 
 */
public interface IdBuilder {

    /**
     * Builds dynamic ID.<br />
     * 
     * Top level ID is ID of some Entity the new ID should be related. Relative name is relative part of the new ID.
     * 
     * @param base top level ID
     * @param name relative name
     * @return new ID from top level ID and relative name
     */
    public String buildID(String base, String name);
}
