/*
 * DummyActivityContextInterfaceFactoryImpl.java
 *
 * Created on 14 Декабрь 2006 г., 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.dummy.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;

import org.mobicents.slee.resource.dummy.ratype.DummyActivity;
import org.mobicents.slee.resource.dummy.ratype.DummyActivityContextInterfaceFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 *
 * @author Oleg Kulikov
 */
public class DummyActivityContextInterfaceFactoryImpl 
        implements DummyActivityContextInterfaceFactory, ResourceAdaptorActivityContextInterfaceFactory {

    // reference to the SLEE for further usage
    private SleeContainer sleeContainer;
    // the JNDI name of the ActivityContextInterfaceFactory object
    private final String jndiName;
    
    /** Creates a new instance of DummyActivityContextInterfaceFactoryImpl */
    public DummyActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer, String name) {
        this.sleeContainer = sleeContainer;
        this.jndiName = "java:slee/resources/" + name + "/dummyacif";
    }

    public String getJndiName() {
        return jndiName;
    }

    public ActivityContextInterface getActivityContextInterface(DummyActivity activity) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        return new ActivityContextInterfaceImpl(this.sleeContainer, activity.getId());
    }
    
}
