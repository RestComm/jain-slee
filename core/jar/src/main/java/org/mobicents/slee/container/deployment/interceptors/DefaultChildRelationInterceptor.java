/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultChildRelationInterceptor.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import javax.slee.ChildRelation;

import org.jboss.logging.Logger;
import org.mobicents.slee.runtime.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class DefaultChildRelationInterceptor implements ChildRelationInterceptor {
    private static Logger logger =  Logger.getLogger( DefaultChildRelationInterceptor.class);
    SbbEntity sbbEntity=null;
    /**
     * 
     */
    public DefaultChildRelationInterceptor() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if(!sbbEntity.getSbbObject().getState().equals(SbbObjectState.READY))
            throw new IllegalStateException("Could not invoke  getChildRelation Method, Object is not in the READY state!");
        String accessorName=method.getName();
        if ( logger.isDebugEnabled()) {
            logger.debug("ChildRelation Interceptor:" + accessorName);
        }
       
        return (ChildRelation) this.sbbEntity.getChildRelation(accessorName);
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#getSbbEntity()
     */
    public SbbEntity getSbbEntity() {        
        return sbbEntity;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#setSbbEntity(gov.nist.slee.runtime.SbbEntity)
     */
    public void setSbbEntity(SbbEntity sbbEntity) {
        //logger.debug("ChildRelation called!");
        this.sbbEntity=sbbEntity;
    }

}
