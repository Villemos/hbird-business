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
package org.hbird.exchange.interfaces;

import org.hbird.exchange.core.Parameter;

/**
 * The base class for all data in the system.
 * 
 * @author Gert Villemos
 * 
 */
public interface IEntityInstance extends IEntity {

    /**
     * Returns timestamp of the {@link IEntityInstance}.
     * 
     * {@link IEntityInstance} is valid at given point in time.
     * For example {@link Parameter} timestamp shows the moment when the value was measured.
     * 
     * @return {@link IEntityInstance} timestamp
     */
    public long getTimestamp();

    /**
     * Returns version of the {@link IEntityInstance}.
     * 
     * Two entity instances can have same ID. To compare instances version is used.
     * If two {@link IEntityInstance} are compared higher version shows newer instance.
     * 
     * @return version of the {@link IEntityInstance}
     */
    public long getVersion();

    /**
     * The Instance ID is a unique string for an object. The ID identifies the entity being referenced
     * (for example the Parameter) not the instance (a parameter value at a specific time).
     * 
     * At any given time there may exist many objects with the same ID, but different versions. These
     * are related; Each represent the same entity (a Parameter), at different points on the timeline (
     * value at time X, value at time Y, ...).
     * 
     * @return The instance ID
     */
    public String getInstanceID();
}
