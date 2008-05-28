/*
 * SmppActivityContextFactoryImpl.java
 *
 * Created on 6 Декабрь 2006 г., 13:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.smpp.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import net.java.slee.resource.smpp.ActivityContextInterfaceFactory;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.ShortMessage;
import net.java.slee.resource.smpp.Transaction;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;


/**
 *
 * @author Oleg Kulikov
 */
public class SmppActivityContextInterfaceFactoryImpl 
        implements ActivityContextInterfaceFactory, ResourceAdaptorActivityContextInterfaceFactory {
    
    private String jndiName;
    private String entityName;
    
    private SleeContainer serviceContainer;
    private ActivityContextFactory activityContextFactory;
    
    private SmppResourceAdaptor ra;
    
    /** Creates a new instance of SmppActivityContextFactoryImpl */
    public SmppActivityContextInterfaceFactoryImpl(SleeContainer serviceContainer, SmppResourceAdaptor ra, String name) {
        this.entityName = name;
        this.jndiName = "java:slee/resources/" + name + "/smppacif";
        this.ra = ra;
        this.serviceContainer = serviceContainer;
        this.activityContextFactory = serviceContainer.getActivityContextFactory();
    }



    public String getJndiName() {
        return jndiName;
    }

    public ActivityContextInterface getActivityContextInterface(Dialog dialog) 
        throws NullPointerException, UnrecognizedActivityException, FactoryException {
        
        return new ActivityContextInterfaceImpl(this.serviceContainer,
                this.activityContextFactory.getActivityContext(
                new SleeActivityHandle(entityName, ra.getActivityHandle(dialog), 
                serviceContainer)).getActivityContextId());
    }

    public ActivityContextInterface getActivityContextInterface(Transaction tx) 
        throws NullPointerException, UnrecognizedActivityException, FactoryException {
        return new ActivityContextInterfaceImpl(this.serviceContainer,
                this.activityContextFactory.getActivityContext(
                new SleeActivityHandle(entityName, ra.getActivityHandle(tx), 
                serviceContainer)).getActivityContextId());
    }
    
}
