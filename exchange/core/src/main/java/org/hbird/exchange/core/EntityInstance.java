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
package org.hbird.exchange.core;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.springframework.data.annotation.Id;

/**
 * The super class of all types being exchanged. Contains a name and a description.
 * The class is intended to be subtyped, into specific types exchanged within the system such as
 * parameters, commands and tasks. Each subtype thus share a set of attributes.
 * 
 * @author Gert Villemos
 * */
public abstract class EntityInstance extends Entity implements IEntityInstance, Comparable<EntityInstance> {

    /** The unique UID of this class. */
    private static final long serialVersionUID = -5803219773253020746L;

    public static final String INSTANCE_ID_SEPARATOR = ":";

    protected long version;

    /**
     * The time at which this object represented a valid state of the system. Default value is the
     * time of creation.
     */
    protected long timestamp;

    @Id
    protected String instanceID;

    private void updateInstanceID() {
        instanceID = new StringBuilder()
                .append(ID)
                .append(INSTANCE_ID_SEPARATOR)
                .append(version)
                .toString();
    }

    /**
     * Constructor of a Named object. The timestamp will be set to the creation time.
     * 
     * @param name The name of the object.
     * @param description The description of the object.
     */
    public EntityInstance(String ID, String name) {
        super(ID, name);
        long now = System.currentTimeMillis();
        this.version = now;
        this.timestamp = now;

        updateInstanceID();
    }

    @Override
    public void setID(String ID) {
        super.setID(ID);
        updateInstanceID();
    }

    /**
     * @return the timestamp
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the version
     */
    @Override
    public long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(long version) {
        this.version = version;
        updateInstanceID();
    }

    @Override
    public String getInstanceID() {
        return instanceID;
    }

    @SuppressWarnings("unchecked")
    public <T extends EntityInstance> T cloneEntity() {
        EntityInstance newInstance = SerializationUtils.clone(this);
        newInstance.setTimestamp(System.currentTimeMillis());
        return (T) newInstance;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", getName());
        builder.append("issuedBy", issuedBy);
        return builder.build();
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(EntityInstance o) {
        if (this == o) {
            return 0;
        }
        return 1;
    }
}
