package org.hbird.business.websockets;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.camel.Body;
import org.springframework.stereotype.Component;

@Component
public class Splitter {

    public <T> Iterator<T> doSplit(@Body Collection<T> collection) {
        return collection.iterator();
    }
    
    public <K, V> Iterator<V> doSplitValues(@Body Map<K, V> map) {
        return doSplit(map.values());
    }
    
    public <K, V> Iterator<K> doSplitKeys(@Body Map<K, V> map) {
        return doSplit(map.keySet());
    }
}
