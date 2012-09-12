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
package org.hbird.business.simpleparametersimulator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.hbird.exchange.core.Parameter;

/**
 * Base type for all simulated parameters. Holds the name and implements the Camel
 * callback method. 
 *
 */
public abstract class BaseParameter {

	protected String name;
	protected String description;
	protected Object value;
	protected String unit;
	
	protected String startTimeFormat = "yyyy-MM-dd HH:mm:ss";
	protected String startTime = null;
	protected long lStartTime = 0l;
	protected long deltaTime = 1000l;
	protected Map<Long, Long> fixedTimes = null;
	protected long offset = 0;
	
	/**
	 * @param name
	 * @param description
	 */
	public BaseParameter(String name, String description) {
		this.name = name;
		this.description = description;
	}

	
	/**
	 * @param name
	 * @param description
	 * @param value
	 * @param unit
	 */
	public BaseParameter(String name, String description, Object value, String unit) {
		this.name = name;
		this.description = description;
		this.value = value;
		this.unit = unit;
	}

	/**
	 * Method to be implemented by specific base classes. Creates the actual parameter
	 * and sets it in the Exchange as the 'in -> body'
	 * 
	 * @param arg0 The exchange into which the parameter should be inserted. To insert a 
	 * parameter into the exchange, use the ExchangeFormatter class.
	 */
	protected abstract Parameter process();	
	
	
	protected long getTime(long tick) {
		if (startTime != null) {
			if (lStartTime == 0) {
				DateFormat format = new SimpleDateFormat(startTimeFormat);
				try {
					lStartTime = format.parse(startTimeFormat).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			return lStartTime + deltaTime*tick + offset; 
		}
		else if (fixedTimes != null) {
			return fixedTimes.get(tick) + offset;
		}
		
		return (new Date()).getTime() + offset;
	}
}
