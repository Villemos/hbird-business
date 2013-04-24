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

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.State;

public class StateRequest extends DataRequest {

    public static final String DESCRIPTION = "A request for the state(s) of a Named object";

    private static final long serialVersionUID = -6214262098953498288L;

    public StateRequest(String issuedBy) {
        super(issuedBy, StateRequest.class.getSimpleName(), DESCRIPTION);
        setClass(State.class.getSimpleName());
    }

    public StateRequest(String issuedBy, String isStateOf) {
        this(issuedBy);
        setIsInitialization(true);
        setArgumentValue(StandardArguments.IS_STATE_OF, isStateOf);
    }

    public StateRequest(String issuedBy, String isStateOf, long from, long to) {
        this(issuedBy, isStateOf);
        setFrom(from);
        setTo(to);
    }

    public StateRequest(String issuedBy, String isStateOf, List<String> names) {
        this(issuedBy, isStateOf);
        addNames(names);
    }

}
