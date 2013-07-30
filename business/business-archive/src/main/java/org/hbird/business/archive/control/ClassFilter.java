package org.hbird.business.archive.control;

import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassFilter extends Filter {
    private static final long serialVersionUID = 5927756133809225735L;
    private static final Logger LOG = LoggerFactory.getLogger(ClassFilter.class);

    protected Set<String> acceptableClasses;
    protected boolean acceptSubclasses;
    
    public ClassFilter(Set<String> acceptableClasses, boolean acceptSubclasses) {
        this.acceptableClasses = new TreeSet<String>(acceptableClasses);
        this.acceptSubclasses = acceptSubclasses;
    }
    
    private boolean checkInterfaces(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        LOG.trace("Checking interfaces for class " + clazz.getName());
        
        for(Class<?> iface : interfaces) {
            LOG.trace(clazz.getName() + " has interface " + iface);

            if(acceptableClasses.contains(iface.getName())) {
                return true;
            }
            
            if(acceptSubclasses && checkInterfaces(iface)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean checkClass(Class<?> clazz) {
        return acceptableClasses.contains(clazz.getName()) || checkInterfaces(clazz);
    }

    @Override
    public boolean passes(Object obj) {
        Class<?> clazz = obj.getClass();
        LOG.trace("Checking object " + obj + " with class " + clazz.getName());
        
        if(acceptSubclasses) {
            do {
                LOG.trace("Checking class " + clazz);
                if(checkClass(clazz)) {
                    return true;
                }
                
                clazz = clazz.getSuperclass();
            } while(clazz.getSuperclass() != null);
            
            return false;
        } else {
            return checkClass(clazz);
        }
    }

}
