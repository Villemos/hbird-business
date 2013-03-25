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

public class Constants {

	/** equatorial radius in meter */
	public static final double EQUATORIAL_RADIUS_OF_THE_EARTH  = 6378137.0;
	
	/** Earth flattening */
	public static final double FLATTEING_OF_THE_ERATH_ON_POLES = 1.0 / 298.257223563; 
	
	/** A central attraction coefficient */ 
	public static final double MU = 3.986004415e+14;                                    
	
	/** Speed of light */ 
	public static final double SPEED_OF_LIGHT = 299792458.0;                            

	protected static OneAxisEllipsoid earth; 

	protected static Frame frame = FramesFactory.getEME2000();
	
	static {
		try {
			earth = new OneAxisEllipsoid(EQUATORIAL_RADIUS_OF_THE_EARTH, FLATTEING_OF_THE_ERATH_ON_POLES, FramesFactory.getITRF2005());
		} catch (OrekitException e) {
			e.printStackTrace();
		}
	}
}
