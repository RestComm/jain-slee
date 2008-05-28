/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.resource.media.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.apache.log4j.Logger;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsResource;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class MediaRaActivityContextInterfaceFactoryImpl implements
        MediaRaActivityContextInterfaceFactory,
        ResourceAdaptorActivityContextInterfaceFactory {

    private static Logger logger = Logger.getLogger(
            MediaRaActivityContextInterfaceFactoryImpl.class);
    // Reference to the SLEE for further usage
    private SleeContainer serviceContainer;
    // The JNDI name of the ActivityContextInterfaceFactory object
    private final String jndiName;
    private String raEntityName;
    private ActivityContextFactory activityContextFactory;

    public MediaRaActivityContextInterfaceFactoryImpl(SleeContainer serviceContainer,
            String entityName) {
        this.serviceContainer = serviceContainer;
        this.activityContextFactory = serviceContainer.getActivityContextFactory();
        this.raEntityName = entityName;
        this.jndiName = "java:slee/resources/" + entityName + "/mediaraacif";
        logger.debug("MediaRaActivityContextInterfaceFactoryImpl.jndiName = " + jndiName);
    }

    public String getJndiName() {
        return jndiName;
    }

    private ActivityContext getActivityContext(Object mediaActivity) {
        MediaActivityHandle handle = new MediaActivityHandle(mediaActivity.toString());
        SleeActivityHandle sleeActivityHandle = new SleeActivityHandle(raEntityName, handle, serviceContainer);
        ActivityContext ac = activityContextFactory.getActivityContext(sleeActivityHandle);
        return ac;
    }

    public ActivityContextInterface getActivityContextInterface(MsSession call) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        ActivityContext ac = getActivityContext(call);
        return new ActivityContextInterfaceImpl(serviceContainer, ac.getActivityContextId());
    }

    public ActivityContextInterface getActivityContextInterface(MsConnection connection) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        ActivityContext ac = getActivityContext(connection);
        return new ActivityContextInterfaceImpl(serviceContainer, ac.getActivityContextId());
    }

    public ActivityContextInterface getActivityContextInterface(MsResource resource) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        ActivityContext ac = getActivityContext(resource);
        return new ActivityContextInterfaceImpl(serviceContainer, ac.getActivityContextId());
    }

//    public ActivityContextInterface getActivityContextInterface(MsTermination termination) throws NullPointerException, UnrecognizedActivityException, FactoryException {
//        ActivityContext ac = getActivityContext(termination);
//        return new ActivityContextInterfaceImpl(serviceContainer, ac.getActivityContextId());
//    }

    public ActivityContextInterface getActivityContextInterface(MsLink link) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        ActivityContext ac = getActivityContext(link);
        return new ActivityContextInterfaceImpl(serviceContainer, ac.getActivityContextId());
    }
}
