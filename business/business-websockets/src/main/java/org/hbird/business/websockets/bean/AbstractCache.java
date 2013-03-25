package org.hbird.business.websockets.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.camel.Body;

public abstract class AbstractCache<T> {

    protected Map<String, T> cache = new ConcurrentSkipListMap<String, T>();
    
    public T updateCache(@Body T newValue) {
        String id = getId(newValue);
        T oldValue = cache.get(id);
        if (oldValue == null || shouldReplace(oldValue, newValue)) {
            cache.put(id, newValue);
        }
        return newValue;
    }
    
    public List<T> getCache(@Body Object object) {
        List<T> list = new ArrayList<T>();
        list.addAll(cache.values());
        return list;
    }

    protected abstract String getId(T t);
    
    protected boolean shouldReplace(T oldValue, T newValue) {
        // by default accept all new values
        // override in subclasses for additional checks. Eg based on timestamp etc.
        return true;
    }
}
