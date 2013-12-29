package org.hbird.business.archive.dao.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hbird.business.api.IDataAccess;
import org.hbird.business.api.exceptions.ArchiveException;
import org.hbird.business.api.exceptions.DataAccessException;
import org.hbird.business.api.exceptions.NotFoundException;
import org.hbird.business.api.exceptions.SaveFailedException;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.State;
import org.hbird.exchange.interfaces.IApplicableTo;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.IEntity;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.Mongo;

public class MongoDataAccess extends MongoDAOBase implements IDataAccess {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDataAccess.class);

    protected Map<String, Set<String>> subtypeRelation = new HashMap<String, Set<String>>();
    protected Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

    public MongoDataAccess(MongoOperations template) {
        super(template);

        initSubtypeRelation("org.hbird");
    }

    // TODO: This is just for testing - instantiate in spring context if possible
    public MongoDataAccess() throws UnknownHostException {
        super(new MongoTemplate(new Mongo("localhost", 27017), DEFAULT_DATABASE_NAME));

        initSubtypeRelation("org.hbird");
    }

    protected void addToSubtypeRelation(Class<?> parent, Class<?> child) {
        String parentName = parent.getName();

        if (!subtypeRelation.containsKey(parentName)) {
            Set<String> subclasses = new HashSet<String>();
            subtypeRelation.put(parentName, subclasses);
        }

        subtypeRelation.get(parentName).add(child.getName());
        classes.put(child.getName(), child);
    }

    protected void initSubtypeRelation(String packageName) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AssignableTypeFilter(IEntity.class));

        Set<BeanDefinition> components = provider.findCandidateComponents(packageName);

        for (BeanDefinition component : components) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends EntityInstance> clazz = (Class<? extends EntityInstance>) (Class.forName(component.getBeanClassName()));
                Class<?> parent = clazz.getSuperclass();

                while (parent != null) {
                    addToSubtypeRelation(parent, clazz);
                    parent = parent.getSuperclass();
                }
            }
            catch (ClassNotFoundException e) {
                LOG.error("Failed to create class by name", e);
            }
        }
    }

    @Override
    public <T extends IEntityInstance> T getById(String id, Class<T> clazz) throws NotFoundException, DataAccessException {
        Query query = new Query(Criteria.where("ID").is(id)).with(sortByVersionDesc);

        return wrapReturn(findOne(query, clazz));
    }

    @Override
    public <T extends IEntityInstance> List<T> getById(String id, long from, long to, Class<T> clazz) throws DataAccessException {
        return getLastVersionOfEachTimestamp(getByFieldInRange(FIELD_ID, id, clazz, from, to));
    }

    @Override
    public <T extends IEntityInstance> T getByInstanceId(String id, Class<T> clazz) throws NotFoundException, DataAccessException {
        return wrapReturn(findById(id, clazz));
    }

    @Override
    public <T extends IEntityInstance> List<T> getAll(Class<T> clazz) throws DataAccessException {
        return getLastVersions(new Query(), clazz);
    }

    @Override
    public Object save(Object o) throws SaveFailedException {
        if (!classes.containsKey(o)) {
            initSubtypeRelation(o.getClass().getPackage().getName());
        }

        try {
            template.save(o);
            return o;
        }
        catch (Exception e) {
            // TODO - 14.08.2013, kimmell - improve exception handling
            LOG.error("Failed to save {}", o, e);
            Throwable tail = e;
            while (tail != null) {
                LOG.error("   Caused by  - {}", tail.getMessage());
                tail = tail.getCause();
            }
            throw new SaveFailedException(o, e);
        }
    }

    @Override
    public List<State> getStates(List<String> names) throws DataAccessException {
        return getLastVersions(query(where(FIELD_NAME).in(names)), State.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IEntityInstance> List<T> getAllBySupertype(Class<T> superclass) throws DataAccessException {
        Query emptyQuery = new Query();
        List<T> result = getLastVersions(emptyQuery, superclass);

        if (subtypeRelation.containsKey(superclass.getName())) {
            for (String subclassName : subtypeRelation.get(superclass.getName())) {
                result.addAll(getLastVersions(emptyQuery, (Class<? extends T>) classes.get(subclassName)));
            }
        }

        return result;
    }

    @Override
    public <T extends IEntityInstance> List<T> getAllInstancesById(String id, Class<T> clazz) throws DataAccessException {
        Query query = query(where(FIELD_ID).is(id));

        return find(query, clazz);
    }

    @Override
    public <T extends IEntityInstance & IApplicableTo> List<T> getApplicableTo(String id, Class<T> clazz) throws DataAccessException {
        // TODO: Specify expected meaning 100%. This is not equal to getApplicableTo(id, clazz, Long.MIN, Long.MAX)
        return getLastVersions(new Query(where(FIELD_APPLICABLE_TO).is(id)), clazz);
    }

    @Override
    public <T extends IEntityInstance & IApplicableTo> List<T> getApplicableTo(String id, Class<T> clazz, long from, long to) throws DataAccessException {
        // Problem: if the query already has a sorting criteria attached, called .with() for
        // another Sort instance does nothing. Maybe a bug in API, TODO: investigate.
        return getLastVersionOfEachTimestamp(getByFieldInRange(FIELD_APPLICABLE_TO, id, clazz, from, to));
    }

    @Override
    public <T extends IEntityInstance & IDerivedFrom> List<T> getDerivedFrom(String instanceID, Class<T> clazz) throws ArchiveException {
        return getLastVersions(new Query(where(FIELD_DERIVED_FROM).is(instanceID)), clazz);
    }

    @Override
    public <T extends IEntityInstance & IDerivedFrom> List<T> getDerivedFrom(String instanceID, Class<T> clazz, long from, long to) throws ArchiveException {
        return getLastVersionOfEachTimestamp(getByFieldInRange(FIELD_DERIVED_FROM, instanceID, clazz, from, to));
    }

    @Override
    public <T extends IEntityInstance> List<T> getIssuedBy(String instanceID, Class<T> clazz) throws ArchiveException {
        return getLastVersions(new Query(where(FIELD_ISSUED_BY).is(instanceID)), clazz);
    }

    @Override
    public <T extends IEntityInstance> List<T> getIssuedBy(String instanceID, Class<T> clazz, long from, long to) throws ArchiveException {
        return getLastVersionOfEachTimestamp(getByFieldInRange(FIELD_ISSUED_BY, instanceID, clazz, from, to));
    }
}
