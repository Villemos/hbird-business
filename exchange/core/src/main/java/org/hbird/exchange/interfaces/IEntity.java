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

/**
 * @author Gert Villemos
 * 
 */
public interface IEntity {
    public void setName(String name);

    public String getDescription();

    public String getIssuedBy();

    /**
     * Method to return the name of the object. The name of the object is a logical
     * label of the object.
     * 
     * The name is not (necessarily) unique within the complete system.
     * 
     * @return
     */
    public String getName();

    /**
     * The ID is a unique string for an entity. The ID identifies the entity being referenced
     * (for example the Parameter) not the instance (a parameter value at a specific time).
     * 
     * At any given time there may exist many objects with the same ID, but different timestamps. These
     * are related; Each represent the same entity (a Parameter), at different points on the timeline (
     * value at time X, value at time Y, ...).
     * 
     * @return The ID
     */
    public String getID();
}
