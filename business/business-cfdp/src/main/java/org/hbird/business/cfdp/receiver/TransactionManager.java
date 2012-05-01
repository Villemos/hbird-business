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

import java.io.File;
import java.io.FileInputStream;

import org.apache.camel.Handler;
import org.hbird.exchange.cfdp.pdus.EofPdu;
import org.hbird.exchange.cfdp.pdus.FilePdu;
import org.hbird.exchange.cfdp.pdus.MetadataPdu;

import com.thoughtworks.xstream.XStream;

public class TransactionManager {

	protected String root = "inbox";

	protected XStream xstream = new XStream();

	/** Find all folders with a EOF file. */
	@Handler
	public void check() {
		File rootFolder = new File(root);
		if (rootFolder.listFiles() == null) {
			return;
		}

		for (File transaction : rootFolder.listFiles()) {
			File eof = new File(transaction.getAbsolutePath() + File.separator + transaction.getName() + "-EOF.pdu");
			if (eof.exists()) {

				MetadataPdu metadata = null;
				int pduLengthSum = 0;
				EofPdu eofPdu = null;
				
				try {
					eofPdu = (EofPdu) xstream.fromXML(new FileInputStream(eof));
					byte[] bytes = new byte[(int) eofPdu.length];

					/** Go through all PDUs and try to assemble the file. */
					for (File pdu : transaction.listFiles()) {

						if (pdu.getName().endsWith("-EOF.pdu")) {
							continue;
						}
						else if (pdu.getName().endsWith("-metadata.pdu")) {
							metadata = (MetadataPdu) xstream.fromXML(new FileInputStream(pdu));
						}
						else {
							FilePdu element = (FilePdu) xstream.fromXML(new FileInputStream(pdu));
							System.arraycopy(element.data, 0, bytes, element.offset, element.length);

							pduLengthSum += element.length;
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				if (pduLengthSum == eofPdu.length) {
					File finalFile = new File(metadata.destinationFileName);

					/** Write file. */
				}
				else {
					/** See if error file already exist. */

				}
			}
		}		
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
}
