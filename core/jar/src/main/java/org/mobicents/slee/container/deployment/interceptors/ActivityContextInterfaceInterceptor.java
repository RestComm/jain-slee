/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ActivityContextInterfaceInterceptor.java
 * 
 * Created on Aug 19, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.slee.ActivityContextInterface;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public interface ActivityContextInterfaceInterceptor extends InvocationHandler {
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable;
    //TODO javadoc to write
    public void setActivityContextInterface(ActivityContextInterface activityContextInterface);
    public ActivityContextInterface getActivityContextInterface();
}
