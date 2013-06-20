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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
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

    private static final long serialVersionUID = -4282345684465911898L;

    /**
     * The time at which this value was generated. Can be the same as the timstamp for 'live' values. Will be different
     * from the
     * timestamp for simulated / predicted values.
     */
    protected long generationTime;

    /** The name of the satellite that this orbital state is applicable to. */
    protected String satelliteId;

    /** Position measured in TODO */
    protected Vector3D position;

    /** Velocity measured in meters / second. */
    protected Vector3D velocity;

    /** Momentum measured in TODO */
    protected Vector3D momentum;

    /** GeoLocation on the Earth. Suitable information for ground track construction. */
    protected GeoLocation geoLocation;

    /** ID of Entity used to calculate this OrbitalState. For example ID of the TLE. */
    protected String derivedFromId;

    /** Flag to show if satellite is in sun light or not. */
    protected boolean inSunLight;

    /** Radio visibility range of the satellite. In meters. */
    protected double range;

    /**
     * Constructor of an orbital state.
     * 
     * @param name The name of the orbital state.
     * @param description A description of the state.
     * @param timestamp The timestamp at which the orbital state is relevant.
     * @param satelliteId
     * @param position The position of the orbit.
     * @param velocity The velocity of the orbit.
     */
    public OrbitalState(String ID, String name) {
        super(ID, name);
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    @Override
    public String getSatelliteID() {
        return satelliteId;
    }

    public Vector3D getMomentum() {
        return momentum;
    }

    public void setMomentum(Vector3D momentum) {
        this.momentum = momentum;
    }

    /**
     * @return the geoLocation
     */
    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    /**
     * @param geoLocation the geoLocation to set
     */
    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * @return the inSunLight
     */
    public boolean isInSunLight() {
        return inSunLight;
    }

    /**
     * @param inSunLight the inSunLight to set
     */
    public void setInSunLight(boolean inSunLight) {
        this.inSunLight = inSunLight;
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
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", getName());
        builder.append("satelliteId", satelliteId);
        return builder.build();
    }

    /**
     * @see org.hbird.exchange.interfaces.IDerivedFrom#getDerivedFrom()
     */
    @Override
    public String getDerivedFromId() {
        return derivedFromId;
    }

    /**
     * @param satelliteId the satelliteId to set
     */
    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }

    /**
     * @param derivedFrom the derivedFrom to set
     */
    public void setDerivedFromId(String derivedFromId) {
        this.derivedFromId = derivedFromId;
    }

    /**
     * @return the range
     */
    public double getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(double range) {
        this.range = range;
    }
}
