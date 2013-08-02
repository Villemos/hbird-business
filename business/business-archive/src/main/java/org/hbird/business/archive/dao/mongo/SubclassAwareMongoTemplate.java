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
package org.hbird.business.archive.dao.mongo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

// TODO: Maybe just querying the hierarchy each time is better if we are going to
// fetch the version from DB anyways
public class SubclassAwareMongoTemplate extends MongoTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(SubclassAwareMongoTemplate.class);

    private static final String HIERARCHY_COLLECTION_NAME = "hbird_hierarchy";
    private static final String SUBCLASS_FIELD = "subclass";
    private static final String SUPERCLASS_FIELD = "superclass";

    private static final String METADATA_COLLECTION_NAME = "hbird_hierarchy_metadata";
    private static final String METADATA_VERSION_FIELD = "version";

    private DBCollection hierarchy;
    private DBCollection metadata;
    private Set<String> knownClasses;
    private Map<String, Set<String>> subclassRelation;
    private Map<String, Class<?>> classloaderCache;

    private long version = 0;

    public SubclassAwareMongoTemplate(Mongo mongo, String databaseName) {
        super(mongo, databaseName);

        setWriteConcern(WriteConcern.ACKNOWLEDGED);

        hierarchy = super.getCollection(HIERARCHY_COLLECTION_NAME);
        metadata = super.getCollection(METADATA_COLLECTION_NAME);

        knownClasses = new HashSet<String>();
        subclassRelation = new HashMap<String, Set<String>>();
        classloaderCache = new HashMap<String, Class<?>>();

        LOG.info("Initializing");

        if (metadata.count() == 0) {
            LOG.info("Metadata collection is empty, creating version marker");
            writeVersionToDB();
        }
        else {
            LOG.info("Version marker found");
            updateCacheFromDB();
            LOG.info("Hierarchy version: " + version);
        }
    }

    private void writeVersionToDB() {
        version = System.currentTimeMillis();
        metadata.save(new BasicDBObject("_id", METADATA_VERSION_FIELD).
                append(METADATA_VERSION_FIELD, version));
    }

    private void updateCacheFromDB() {
        LOG.trace("Updating hierarchy from DB, local version is {}", version);
        DBObject versionDoc = metadata.findOne();
        LOG.trace("DB version marker object: {}", versionDoc);

        // Extra check in case the metadata collection is dropped after the construction
        long dbVersion = versionDoc == null ? 0 : (Long) versionDoc.get(METADATA_VERSION_FIELD);
        LOG.trace("DB version: {}", dbVersion);

        if (dbVersion == version) {
            return;
        }

        DBCursor cursor = hierarchy.find();

        for (DBObject obj : cursor) {
            String subclass = (String) obj.get(SUBCLASS_FIELD);
            String superclass = (String) obj.get(SUPERCLASS_FIELD);

            if (!knownClasses.contains(subclass)) {
                try {
                    addToRelationCache(Class.forName(subclass), superclass);
                    knownClasses.add(subclass);
                }
                catch (ClassNotFoundException e) {
                    LOG.error("Failed to resolve hierarchy class by name: ", e);
                }
            }
        }

        version = dbVersion;
    }

    private void addToRelationCache(Class<?> subclass, String superclass) {
        String subName = subclass.getName();
        classloaderCache.put(subName, subclass);

        if (!subclassRelation.containsKey(superclass)) {
            Set<String> subclasses = new HashSet<String>();
            subclasses.add(subName);

            subclassRelation.put(superclass, subclasses);
        }
        else {
            subclassRelation.get(superclass).add(subName);
        }
    }

    private void saveRelation(Class<?> subclass, String superclass) {
        BasicDBObject object = new BasicDBObject(SUBCLASS_FIELD, subclass.getName());
        object.append(SUPERCLASS_FIELD, superclass);

        hierarchy.insert(object);
        addToRelationCache(subclass, superclass);
    }

    private void saveIntoHierarchy(Class<?> clazz) {
        if (!subclassRelation.containsKey(clazz.getName())) {
            subclassRelation.put(clazz.getName(), new HashSet<String>());
        }

        if (knownClasses.contains(clazz.getName())) {
            return;
        }

        Class<?> sup = clazz.getSuperclass();

        while (sup != null) {
            saveRelation(clazz, sup.getName());

            saveIntoHierarchy(sup);
            writeVersionToDB();

            sup = sup.getSuperclass();
        }

        knownClasses.add(clazz.getName());
    }

    private void addToHierarchy(Class<?> clazz) {
        updateCacheFromDB();
        saveIntoHierarchy(clazz);
    }

    @Override
    public <T> T findOne(Query query, Class<T> clazz) {
        addToHierarchy(clazz);

        T result = super.findOne(query, clazz);

        if (result != null) {
            return result;
        }

        for (String subclassName : subclassRelation.get(clazz.getName())) {
            Class<? extends T> subclass = (Class<? extends T>) classloaderCache.get(subclassName);

            result = super.findOne(query, subclass);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @Override
    public <T> List<T> find(Query query, Class<T> clazz) {
        addToHierarchy(clazz);

        List<T> result = super.find(query, clazz);

        assert subclassRelation.containsKey(clazz.getName());
        assert subclassRelation.get(clazz.getName()) != null;

        for (String subclassName : subclassRelation.get(clazz.getName())) {
            Class<? extends T> subclass = (Class<? extends T>) classloaderCache.get(subclassName);

            result.addAll(super.find(query, subclass));
        }

        return result;
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        addToHierarchy(clazz);

        List<T> result = super.findAll(clazz);

        for (String subclassName : subclassRelation.get(clazz.getName())) {
            Class<? extends T> subclass = (Class<? extends T>) classloaderCache.get(subclassName);

            result.addAll(super.findAll(subclass));
        }

        return result;
    }

    public void save(Object toSave) {
        addToHierarchy(toSave.getClass());

        super.save(toSave);
    }
}
