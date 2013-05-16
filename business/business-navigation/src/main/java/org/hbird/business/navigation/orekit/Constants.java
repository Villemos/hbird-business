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
package org.hbird.business.navigation.orekit;

import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;

/**
 * @author Gert Villemos
 * 
 */
public class Constants {

    /** Equatorial radius of Earth in meters. */
    public static final double EQUATORIAL_RADIUS_OF_THE_EARTH = org.orekit.utils.Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
    public static final double EQUATORIAL_RADIUS_OF_SUN = org.orekit.utils.Constants.SUN_RADIUS; // was 696000000. in
                                                                                                 // ESTCube. Not exactly
                                                                                                 // the same value ...
    /** Earth flattening */
    public static final double FLATTEING_OF_THE_ERATH_ON_POLES = org.orekit.utils.Constants.WGS84_EARTH_FLATTENING;

    /** A central attraction coefficient m<sup>3</sup>/s<sup>2</sup>. */
    public static final double MU = org.orekit.utils.Constants.WGS84_EARTH_MU;

    /** Speed of light m/s. */
    public static final double SPEED_OF_LIGHT = org.orekit.utils.Constants.SPEED_OF_LIGHT;

    public static final int MINUTES_PER_DAY = 60 * 24;
    public static final int SECONDS_PER_DAY = 60 * MINUTES_PER_DAY;

    // constants for orbit number calculation from TLE
    public static final double NORMALIZED_EQUATORIAL_RADIUS = 1.0;
    public static final double XKE = 0.0743669161331734132;
    public static final double XJ2 = 1.082616e-3;
    public static final double CK2 = 0.5 * XJ2 * NORMALIZED_EQUATORIAL_RADIUS * NORMALIZED_EQUATORIAL_RADIUS;

    public static OneAxisEllipsoid earth;

    public static Frame FRAME = FramesFactory.getEME2000();

    static {
        try {
            earth = new OneAxisEllipsoid(EQUATORIAL_RADIUS_OF_THE_EARTH, FLATTEING_OF_THE_ERATH_ON_POLES, FramesFactory.getITRF2005());
        }
        catch (OrekitException e) {
            throw new RuntimeException("Failed to create EARTH constant in " + Constants.class.getName(), e);
        }
    }
}
