/*
 * File Name     : JccActivityContextInterfaceFactoryImpl.java
 *
 * The Java Call Control RA
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

package org.mobicents.slee.resource.jcc.ra;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

import org.mobicents.slee.resource.jcc.ratype.JccActivityContextInterfaceFactory;

import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccConnection;

/**
 * Represents the Context Interface Factory Object.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccActivityContextInterfaceFactoryImpl 
        implements JccActivityContextInterfaceFactory, ResourceAdaptorActivityContextInterfaceFactory {
    
    private SleeContainer sleeContainer;
    private String jndiName;
    
    /** Creates a new instance of JccActivityContextInterfaceFactoryImpl */
    public JccActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer, String name) {
        this.sleeContainer = sleeContainer;
        this.jndiName = "java:slee/resources/" + name + "/ActivityContextInterfaceFactory";
    }

    public ActivityContextInterface getActivityContextInterface(JccCall call) 
    throws NullPointerException, UnrecognizedActivityException, FactoryException {
        return new ActivityContextInterfaceImpl(call.toString());
    }

    public ActivityContextInterface getActivityContextInterface(JccConnection connection) 
    throws NullPointerException, UnrecognizedActivityException, FactoryException {
        return new ActivityContextInterfaceImpl(connection.toString());
    }

    public String getJndiName() {
        return jndiName;
    }
    
}
