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

import static org.hbird.exchange.dataaccess.Arguments.APPLICABLE_TO;
import static org.hbird.exchange.dataaccess.Arguments.CLASS;
import static org.hbird.exchange.dataaccess.Arguments.DELETE_ALL;
import static org.hbird.exchange.dataaccess.Arguments.DELTA_PROPAGATION;
import static org.hbird.exchange.dataaccess.Arguments.DERIVED_FROM;
import static org.hbird.exchange.dataaccess.Arguments.FROM;
import static org.hbird.exchange.dataaccess.Arguments.ISSUED_BY;
import static org.hbird.exchange.dataaccess.Arguments.IS_STATE_OF;
import static org.hbird.exchange.dataaccess.Arguments.NAMES;
import static org.hbird.exchange.dataaccess.Arguments.SATELLITE_NAME;
import static org.hbird.exchange.dataaccess.Arguments.TO;
import static org.hbird.exchange.dataaccess.Arguments.create;

import java.util.List;

import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.CommandArgument;

public class DeletionRequest extends DataRequest {

    public static final String DESCRIPTION = "A request to delete part or all data in an archive.";

    private static final long serialVersionUID = -811265458963306125L;

    public DeletionRequest(String issuedBy) {
        super(issuedBy, DeletionRequest.class.getSimpleName(), DESCRIPTION);
    }

    public DeletionRequest(String issuedBy, boolean deleteAll) {
        this(issuedBy);
        setArgumentValue(StandardArguments.DELETE_ALL, deleteAll);
    }

    /**
     * @see org.hbird.exchange.core.Command#getArgumentDefinitions()
     */
    @Override
    protected List<CommandArgument> getArgumentDefinitions(List<CommandArgument> args) {
        args = super.getArgumentDefinitions(args);
        args.add(create(DELETE_ALL));
        args.add(create(APPLICABLE_TO));
        args.add(create(CLASS));
        args.add(create(DELTA_PROPAGATION));
        args.add(create(DERIVED_FROM));
        args.add(create(ISSUED_BY));
        args.add(create(FROM));
        args.add(create(IS_STATE_OF));
        args.add(create(NAMES));
        args.add(create(SATELLITE_NAME));
        args.add(create(TO));
        return args;
    }

    /**
     * @return
     */
    public boolean isDeleteAll() {
        return getArgumentValue(StandardArguments.DELETE_ALL, Boolean.class);
    }

    public void setDeleteAll() {
        setDeleteAll(true);
    }

    public void setDeleteAll(boolean deleteAll) {
        setArgumentValue(StandardArguments.DELETE_ALL, deleteAll);
    }
}
