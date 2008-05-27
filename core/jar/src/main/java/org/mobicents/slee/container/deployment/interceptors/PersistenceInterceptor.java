/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * PersistenceInterceptor.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import org.mobicents.slee.runtime.sbbentity.SbbEntity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public interface PersistenceInterceptor extends InvocationHandler{
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable ;
    public SbbEntity getSbbEntity();
    public void setSbbEntity(SbbEntity sbbEntity);
}
