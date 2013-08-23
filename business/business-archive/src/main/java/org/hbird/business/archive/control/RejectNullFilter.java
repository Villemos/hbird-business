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
package org.hbird.business.archive.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RejectNullFilter extends Filter {

    private static final long serialVersionUID = -343709890356373785L;

    private static final Logger LOG = LoggerFactory.getLogger(RejectNullFilter.class);

    private boolean logWarning;

    /**
     * @see org.hbird.business.archive.control.Filter#passes(java.lang.Object)
     */
    @Override
    public boolean passes(Object obj) {
        if (obj == null) {
            if (logWarning) {
                LOG.warn("Rejecting null object");
            }
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * @param logStackTrace the logStackTrace to set
     */
    public void setLogWarning(boolean logWarning) {
        this.logWarning = logWarning;
    }
}
