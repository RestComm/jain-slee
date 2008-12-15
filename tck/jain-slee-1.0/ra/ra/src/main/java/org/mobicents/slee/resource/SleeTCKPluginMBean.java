/*
 * Created on 14-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.resource;

import javax.management.ObjectName;

/**
 * @author usuario
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface SleeTCKPluginMBean {

    /**
     * The identity of the MBean within the context of
     * the default domain.
     */
    public static final String JMX_ID = ":name=SleeTCKPlugin";

    /**
     * Returns the JMX ObjectName of the vendor's
     * SleeManagementMBean.
     *
     * This method does not throw any Exceptions.
     */
    public ObjectName getSleeManagementMBean();

}