package org.mobicents.slee.resource.persistence.ratype;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;


public abstract  class SbbEntityManager implements EntityManager {

	
	
	
	public abstract String getID();
	

	public abstract void clear() ;

	public abstract void close() ;

	public abstract boolean contains(Object arg0) ;

	public abstract Query createNamedQuery(String arg0) ;

	public abstract Query createNativeQuery(String arg0) ;

	public abstract Query createNativeQuery(String arg0, Class arg1);

	public abstract Query createNativeQuery(String arg0, String arg1) ;

	public abstract Query createQuery(String arg0) ;

	public abstract Object find(Class arg0, Object arg1) ;

	public abstract void flush() ;

	public abstract Object getDelegate() ;

	public abstract FlushModeType getFlushMode() ;

	public abstract Object getReference(Class arg0, Object arg1) ;

	public abstract EntityTransaction getTransaction() ;

	public abstract boolean isOpen() ;

	public abstract void joinTransaction() ;

	public abstract void lock(Object arg0, LockModeType arg1) ;

	public abstract Object merge(Object arg0);

	public abstract void persist(Object arg0);

	public abstract void refresh(Object arg0) ;

	public abstract void remove(Object arg0) ;

	public abstract void setFlushMode(FlushModeType arg0) ;
	
}
