/*
 * Created on Dec 1, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.management.SleeState;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.tck.TCKActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivityContextInterfaceFactory;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class TCKActivityContextInterfaceFactoryImpl implements
        TCKActivityContextInterfaceFactory, ResourceAdaptorActivityContextInterfaceFactory {
    private SleeContainer sleeContainer;
    private final String jndiName = "java:slee/resources/tckacif";
    private ActivityContextFactory activityContextFactory;
    private String raEntityName;
    private static Logger log;
    
    static {
        log = Logger.getLogger(TCKActivityContextInterfaceFactoryImpl.class);
    }
    
    public TCKActivityContextInterfaceFactoryImpl(SleeContainer svcContainer, String entityName) {
        this.sleeContainer = svcContainer;
        activityContextFactory = svcContainer.getActivityContextFactory();
        this.raEntityName = entityName;
        
    }

    public ActivityContextInterface getActivityContextInterface(
            TCKActivity activity) throws UnrecognizedActivityException,
            FactoryException {
        if (activity == null)
            throw new NullPointerException("null activity ! huh!!");
        
        // if SLEE is not in RUNNING state ActivityContexts cannot be obtained
        if (!sleeContainer.getSleeState().equals(SleeState.RUNNING)) return null;
        
        log.debug("Getting AC ID for TCK Activity: " + activity);
        String acId = this.activityContextFactory.getActivityContext(new SleeActivityHandle(raEntityName, new TCKActivityHandle(activity.getID()), this.sleeContainer)).getActivityContextId();
        log.debug("TCK RA ACI Factory getting interface for AC: " + acId);
        return new ActivityContextInterfaceImpl(acId);
        
        

    }

    public String getJndiName() {
        // TODO Auto-generated method stub
        return jndiName;
    }

}