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
package org.hbird.business.archive.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

public class ClassDeserializer implements Converter<DBObject, Class<?>> {
    private static final Logger LOG = LoggerFactory.getLogger(ClassDeserializer.class);

    @Override
    public Class<?> convert(DBObject obj) {
        try {
            String name = (String) obj.get(ClassSerializer.CLASS_NAME_FIELD);
            return Class.forName(name);
        }
        catch (Exception e) {
            LOG.error("Failed to read class", e);

            return Object.class;
        }
    }

}
