/*
 * The Short Message Service resource adaptor type
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

package net.java.slee.resource.smpp;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * Implements ActivityContextInterfaceFactory interface.
 *
 * @author Pavel Mitrenko
 * @author Oleg Kulikov
 */
public interface ActivityContextInterfaceFactory {
    public ActivityContextInterface getActivityContextInterface(Dialog dialog)
        throws NullPointerException, UnrecognizedActivityException, FactoryException;
    public ActivityContextInterface getActivityContextInterface(Transaction tx)
        throws NullPointerException, UnrecognizedActivityException, FactoryException;
    
}
