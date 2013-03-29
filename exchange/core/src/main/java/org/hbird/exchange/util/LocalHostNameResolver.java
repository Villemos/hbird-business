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
package org.hbird.exchange.util;

import java.net.UnknownHostException;

import org.apache.camel.util.InetAddressUtil;
import org.hbird.exchange.core.BusinessCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for resolving local host name using {@link InetAddressUtil}.
 * Resolved host name can be used to identify current host.
 * 
 * In case host name resolving fails default value {@value #DEFAULT_LOCAL_HOST_NAME} is returned.
 * 
 * @see BusinessCard
 */
public class LocalHostNameResolver {

    /** Default host name used in case resolving local host name fails. */
    public static final String DEFAULT_LOCAL_HOST_NAME = "localhost";

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(LocalHostNameResolver.class);

    /** Resolved host name. */
    private static String resolvedHostName = resolveLocalHostName(DEFAULT_LOCAL_HOST_NAME);

    /**
     * Returns resolved host name for the local host.
     * 
     * @return resolved host name for the local host.
     */
    public static String getLocalHostName() {
        return resolvedHostName;
    }

    /**
     * Resolves host name of the local host.
     * 
     * @param defaultValue value to return in case resolving fails.
     * @return resolved host name or default value
     * @see InetAddressUtil
     */
    static String resolveLocalHostName(String defaultValue) {
        try {
            LOG.debug("Resolving local host name");
            String hostname = InetAddressUtil.getLocalHostName();
            LOG.info("Local host name: {}", hostname);
            return hostname;
        }
        catch (UnknownHostException uhe) {
            LOG.warn("Failed to resolve local host name; using default value: {}", defaultValue);
            return defaultValue;
        }
    }
}
