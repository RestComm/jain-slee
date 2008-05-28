/*
 * ActivityContextInterfaceFactory.java
 *
 * Media Gateway Control Protocol (MGCP) Resource Adaptor Type.
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

package net.java.slee.resource.mgcp;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * ActivityContextInterfaceFactory interface.
 *
 * @see JAIN SLEE 1.0 Specification, Final Release, p244.
 * @author Oleg Kulikov
 * @author eduardomartins
 */
public interface MgcpActivityContextInterfaceFactory {

    /**
     * Gets ActivityContextInterface for endpoint activity.
     *
     * @param activity
     *  the endpoint activity object.
     * @return the ActivityContextInterface.
     */
    public ActivityContextInterface getActivityContextInterface(MgcpEndpointActivity activity)
        throws NullPointerException, UnrecognizedActivityException, FactoryException;
    
    /**
     * Gets ActivityContextInterface for connection activity.
     *
     * @param activity the connection activity object.
     * @return the ActivityContextInterface.
     */
    public ActivityContextInterface getActivityContextInterface(MgcpConnectionActivity activity)
        throws NullPointerException, UnrecognizedActivityException, FactoryException;
    
}
