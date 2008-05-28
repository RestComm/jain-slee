/*
 * MgcpActivityContextInterfaceFactoryImpl.java
 *
 * Media Gateway Control Protocol (MGCP) Resource Adaptor.
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

package org.mobicents.slee.resource.mgcp.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ActivityHandle;

import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 *
 * @author Oleg Kulikov
 * @author eduardomartins
 */
public class MgcpActivityContextInterfaceFactoryImpl implements MgcpActivityContextInterfaceFactory, ResourceAdaptorActivityContextInterfaceFactory {
  
    private String jndiName;
    private String raEntityName;
    
    private SleeContainer serviceContainer;
    private ActivityContextFactory activityContextFactory;
    
    private MgcpResourceAdaptor ra;
    
    
    /** Creates a new instance of MgcpActivityContextInterfaceFactoryImpl */
    public MgcpActivityContextInterfaceFactoryImpl(SleeContainer serviceContainer, MgcpResourceAdaptor ra, String name) {
        this.raEntityName = name;
        this.jndiName = "java:slee/resources/" + name + "/mgcpacif";
        this.ra = ra;
        this.serviceContainer = serviceContainer;
        this.activityContextFactory = serviceContainer.getActivityContextFactory();
    }

    public String getJndiName() {
        return jndiName;
    }

    public ActivityContextInterface getActivityContextInterface(MgcpConnectionActivity activity) throws NullPointerException, UnrecognizedActivityException, FactoryException {
    	 return getAci(activity);
    }

    public ActivityContextInterface getActivityContextInterface(MgcpEndpointActivity activity) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        return getAci(activity);
    }
    
    private ActivityContextInterface getAci(Object activity) throws UnrecognizedActivityException, NullPointerException {
    	if (activity != null) {
    		ActivityHandle activityHandle = ra.getActivityHandle(activity);
        	if (activityHandle != null) {
        		SleeActivityHandle sleeActivityHandle = new SleeActivityHandle(raEntityName, activityHandle, serviceContainer);
            	ActivityContext ac = activityContextFactory.getActivityContext(sleeActivityHandle);
            	return new ActivityContextInterfaceImpl(serviceContainer, ac.getActivityContextId());
        	}
        	else {
        		throw new UnrecognizedActivityException(activity);
        	}
    	}
    	else {
    		throw new NullPointerException("activity can't be null");
    	}
    }

}
