/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultUsageInterceptor.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import org.mobicents.slee.runtime.sbbentity.SbbEntity;

import java.lang.reflect.Method;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class DefaultUsageInterceptor implements UsageParameterInterceptor {
    SbbEntity sbbEntity=null;
    /**
     * 
     */
    public DefaultUsageInterceptor() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#getSbbEntity()
     */
    public SbbEntity getSbbEntity() {
        // TODO Auto-generated method stub
        return sbbEntity;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#setSbbEntity(gov.nist.slee.runtime.SbbEntity)
     */
    public void setSbbEntity(SbbEntity sbbEntity) {
        // TODO Auto-generated method stub
        this.sbbEntity=sbbEntity;
    }
}
