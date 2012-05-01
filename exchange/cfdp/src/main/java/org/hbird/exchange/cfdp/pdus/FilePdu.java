/**
 * Created by Villemos Solutions (www.villemos.com), 2012.
 * 
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
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
package org.hbird.exchange.cfdp.pdus;

public class FilePdu extends Pdu {

	public byte[] data;
	public int length = 0;
	public long segmentID;

	public int offset;
	
	public FilePdu(String destination, String transactionID, byte[] data, int length, long segmentID) {
		super(destination, transactionID);
		this.data = data;
		this.length = length;
		this.segmentID = segmentID;
	}
	
	{
		this.type = "FILE";
	}
}
