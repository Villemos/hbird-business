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


/**
 * Class holding binary data.
 * 
 * @author Admin
 *
 */
public class Binary extends Named {

	private static final long serialVersionUID = -5513523616365151215L;

	/** The raw binary data */
	protected byte[] rawdata = null;

	/**
	 * Constructor
	 * 
	 * @param issuedBy The name of the system element that issued this binary data
	 * @param name Name of this binary data
	 * @param type The type of the data
	 * @param description A description of the data
	 * @param rawdata The raw data
	 */
	public Binary(String issuedBy, String name, String type, String description, byte[] rawdata) {
		super(issuedBy, name, type, description);
		
		this.rawdata = rawdata;
	}

	public byte[] getRawdata() {
		return rawdata;
	}

	public void setRawdata(byte[] rawdata) {
		this.rawdata = rawdata;
	}
	
	
}
