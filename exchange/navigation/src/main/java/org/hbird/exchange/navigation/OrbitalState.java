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
package org.hbird.exchange.navigation;

import org.hbird.exchange.core.D3Vector;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.IGenerationTimestamped;
import org.hbird.exchange.interfaces.ISatelliteSpecific;

/**
 * Class describing a specific orbital state at a give point in time.
 * The state is defined through
 * position. A vector defining the location of the body.
 * velocity. A vector defining the velocity of the body.
 * momentum. A vector defining the momentum of the body.
 * timestamp. The time the body have this state. May be in the future.
 */
public class OrbitalState extends EntityInstance implements IGenerationTimestamped, ISatelliteSpecific, IDerivedFrom {

    /** The unique UID. */
    private static final long serialVersionUID = -8112421610188582037L;

    /**
     * The time at which this value was generated. Can be the same as the timstamp for 'live' values. Will be different
     * from the
     * timestamp for simulated / predicted values.
     */
    protected long generationTime = 0;

    /** The name of the satellite that this orbital state is applicable to. */
    protected String satelliteId;

    /** Position measured in TODO */
    protected D3Vector position;

    /** Velocity measured in meters / second. */
    protected D3Vector velocity;

    /** Momentum measured in TODO */
    protected D3Vector momentum;

    protected String derivedFrom;

    /**
     * Constructor of an orbital state.
     * 
     * @param name The name of the orbital state.
     * @param description A description of the state.
     * @param timestamp The timestamp at which the orbital state is relevant.
     * @param satellite
     * @param position The position of the orbit.
     * @param velocity The velocity of the orbit.
     */
    public OrbitalState(String issuedBy, String name, String description, long timestamp, long generationTime, String satelliteId, D3Vector position,
            D3Vector velocity, D3Vector momentum, EntityInstance derivedFrom) {
        super(issuedBy, name, description);
        this.satelliteId = satelliteId;
        this.position = position;
        this.velocity = velocity;
        this.momentum = momentum;
        this.generationTime = generationTime;
        this.timestamp = timestamp;
        this.derivedFrom = derivedFrom.getID();
    }

    public OrbitalState(String issuedBy, String name, String description, long timestamp, String satelliteId, D3Vector position, D3Vector velocity,
            D3Vector momentum, EntityInstance derivedFrom) {
        super(issuedBy, name, description);
        this.satelliteId = satelliteId;
        this.position = position;
        this.velocity = velocity;
        this.momentum = momentum;
        this.timestamp = timestamp;
        this.derivedFrom = derivedFrom.getID();
    }

    public D3Vector getPosition() {
        return position;
    }

    public void setPosition(D3Vector position) {
        this.position = position;
    }

    public D3Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(D3Vector velocity) {
        this.velocity = velocity;
    }

    @Override
    public String getSatelliteId() {
        return satelliteId;
    }

    public D3Vector getMomentum() {
        return momentum;
    }

    public void setMomentum(D3Vector momentum) {
        this.momentum = momentum;
    }

    @Override
    public long getGenerationTime() {
        return generationTime;
    }

    @Override
    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    @Override
    public String prettyPrint() {
        return String.format("%s[ID=%s, name=%s, timestamp=%s, satellite=%s]", this.getClass().getSimpleName(), getInstanceID(), getName(), timestamp,
                satelliteId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hbird.exchange.interfaces.IDerivedFrom#getDerivedFrom()
     */
    @Override
    public String getDerivedFrom() {
        return derivedFrom;
    }
}
