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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hbird.exchange.interfaces.IApplicableTo;

/**
 * 
 */
public class ApplicableTo extends EntityInstance implements IApplicableTo {

    private static final long serialVersionUID = 4231408031952256393L;

    protected String applicableTo;

    /**
     * @param ID
     * @param name
     */
    public ApplicableTo(String ID, String name) {
        super(ID, name);
    }

    /**
     * @see org.hbird.exchange.interfaces.IApplicableTo#getApplicableTo()
     */
    @Override
    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("ID", getInstanceID());
        builder.append("name", getName());
        builder.append("applicableTo", getApplicableTo());
        return builder.build();
    }
}
