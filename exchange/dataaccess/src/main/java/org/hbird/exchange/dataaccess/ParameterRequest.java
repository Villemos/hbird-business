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
package org.hbird.exchange.dataaccess;

import org.hbird.exchange.core.Parameter;

public class ParameterRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the latest value of a parameter";

    private static final long serialVersionUID = -210475353836504189L;

    public ParameterRequest(String ID) {
        super(ID, ParameterRequest.class.getSimpleName());
        setDescription(DESCRIPTION);
        setClass(Parameter.class.getSimpleName());
    }
}
