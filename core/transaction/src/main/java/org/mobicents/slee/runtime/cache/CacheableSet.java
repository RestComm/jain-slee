/*
 * CacheableSet.java
 * 
 * Created on Jul 26, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.runtime.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * tx aware cacheable set.
 * Wraps around CacheableMap.
 * 
 * @see CacheableMap 
 */
public class CacheableSet implements Set, Serializable  {

	/**
	 * SID is a good practice for serialization
	 */
	private static final long serialVersionUID = -4925272152876951668L;

	private CacheableMap cmap;
	
    // private transient static Logger logger = Logger.getLogger(CacheableSet.class);
    
    /**
     * Creates an instance of a tx aware cacheable set. 
     * 
     * @param key --
     *            the unique set key in the enclosing cache space 
     */
    public CacheableSet(Object setKey) {
        this.cmap = new CacheableMap(setKey); 
    }
    
    public String toString() {
        return cmap.keySet().toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#size()
     */
    public int size() {
        return cmap.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#clear()
     */
    public void clear() {
    	cmap.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty() {
        return cmap.isEmpty();

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#toArray()
     */
    public Object[] toArray() {
        return cmap.keySet().toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#add(java.lang.Object)
     */
    public boolean add(Object obj) {
    	if (cmap.containsKey(obj)) {
            return false;
        } else {
        	cmap.put(obj, CacheableMap.NULL_VALUE);
            return true;
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(Object obj) {
        return cmap.containsKey(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#remove(java.lang.Object)
     */
    public boolean remove(Object obj) {
        boolean retval = cmap.containsKey(obj);
        if (retval) {
        	cmap.remove(obj);
        }
        return retval;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#addAll(java.util.Collection)
     */
    public boolean addAll(Collection c) {
        boolean retval = false;
        for (Iterator it = c.iterator(); it.hasNext();) {
            Object obj = it.next();
            if (!cmap.containsKey(obj)) {
                retval = true;
                cmap.put(obj, null);
            }
        }
        return retval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection c) {
        return cmap.keySet().containsAll(c);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection c) {
        boolean retval = false;

        for (Iterator it = c.iterator(); it.hasNext();) {
            Object obj = it.next();
            if (cmap.containsKey(obj)) {
                retval = true;
                cmap.remove(obj);
            }
        }
        return retval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException("Unsupported operation");

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#iterator()
     */
    public Iterator iterator() {
        return cmap.keySet().iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Set#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] obj) {
    	return cmap.keySet().toArray(obj);
    }
    
    /**
     * requests removal of the whole set from the cache
     *
     */
    public void remove() {
    	cmap.remove();
    }

    @Override
    public boolean equals(Object obj) {    	
    	if(obj != null && obj.getClass() == this.getClass() && cmap.keySet().equals(((CacheableSet)obj).cmap.keySet())) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public int hashCode() {
    	return cmap.hashCode();
    }
}

