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

import java.util.Map;

import org.hbird.exchange.interfaces.IApplicableTo;
import org.hbird.exchange.interfaces.INamed;

/**
 * Class for metadata to another Named object. The object 'points' to the subject through the
 * 'subject' field. Currently the metadata can only be applicable to a single other obejct.
 * 
 * @author Gert Villemos
 * 
 */
public class Metadata extends Named implements IApplicableTo {

    public static final String DESCRIPTION = "Metadata applicable to another Named object.";

    private static final long serialVersionUID = 2804335094757225970L;

    /** The identifier of the object that this is applicable too. */
    protected String subject = null;

    /** The metadata itself. Can be any type, keyed on string. */
    protected Map<String, Object> metadata = null;

    /**
     * Constructor to create metadata for a specific object.
     * 
     * @param issuedBy The name of the entity who have issued this metadata
     * @param name The name of the metadata. Can be used to give logical names such as 'documentation' or 'note'.
     * @param toSubject A Named object that this metadata is applicable to.
     * @param metadata The metadata. Map keyed on a string ID and hold an object.
     */
    public Metadata(String issuedBy, String name, INamed toSubject, Map<String, Object> metadata) {
        super(issuedBy, name, DESCRIPTION);
        this.subject = toSubject.getID();
        this.metadata = metadata;
    }

    public String applicableTo() {
        return subject;
    }

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
    
    
}
