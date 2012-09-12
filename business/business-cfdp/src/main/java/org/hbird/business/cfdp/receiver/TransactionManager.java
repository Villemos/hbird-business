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
package org.hbird.business.cfdp.receiver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.camel.Handler;
import org.hbird.exchange.cfdp.pdus.EofPdu;
import org.hbird.exchange.cfdp.pdus.FilePdu;
import org.hbird.exchange.cfdp.pdus.MetadataPdu;

import com.thoughtworks.xstream.XStream;

public class TransactionManager {

	protected String root = "inbox";

	protected long latency = 60000;
	
	protected XStream xstream = new XStream();

	/** Find all folders with a EOF file. */
	@Handler
	public void check() {
		File rootFolder = new File(root);
		if (rootFolder.listFiles() == null) {
			return;
		}

		try {
			for (File transaction : rootFolder.listFiles(new EofFileFilter())) {

				/** Get the EOF PDU. */
				EofPdu eofPdu = (EofPdu) xstream.fromXML(new FileInputStream(transaction.getName() + "-EOF.pdu"));

				/** Check if we have all segments. */
				if (eofPdu.segments == transaction.listFiles().length - 2) {

					/** Get the metadata PDU. */
					MetadataPdu metadata = (MetadataPdu) xstream.fromXML(new FileInputStream(transaction.getName() + "-metadata.pdu"));

					/** Assign the necesarry byte array to hold all segments. */
					int pduLengthSum = 0;			
					byte[] bytes = new byte[(int) eofPdu.length];

					/** Go through all PDUs and assemble the file. */
					for (File pdu : transaction.listFiles()) {
						if (pdu.getName().endsWith("-EOF.pdu") == false && pdu.getName().endsWith("-metadata.pdu") == false) {
							FilePdu element = (FilePdu) xstream.fromXML(new FileInputStream(pdu));
							System.arraycopy(element.data, 0, bytes, element.offset, element.length);
							pduLengthSum += element.length;
						}
					}

					if (pduLengthSum == eofPdu.length) {
						File finalFile = new File(metadata.destinationFileName);

						/** Write file. */
						FileOutputStream fos = new FileOutputStream(finalFile);
						BufferedOutputStream bos = new BufferedOutputStream(fos);
						bos.write(bytes);
					}
				}
				else {
					/** Check if we have an error log. */
					File errorLog = new File(root + File.separator + "errors" + File.separator + transaction.getName() + ".log");
					Date now = new Date();
					if (errorLog.exists()) {
						TransferLog log = (TransferLog) xstream.fromXML(new FileInputStream(errorLog));
						if (log.timestamp + latency > now.getTime()) {
							/** Transfer failed. */
						}
					}
					else {
						/** Create log. */
						xstream.toXML(new TransferLog(now.getTime()), new FileOutputStream(errorLog));
					}
				}
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
}
