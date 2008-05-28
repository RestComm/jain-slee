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
package org.mobicents.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public interface SipActivityContextInterfaceFactory {
    public ActivityContextInterface getActivityContextInterface(
            ClientTransaction clientTransaction) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;

    public ActivityContextInterface getActivityContextInterface(
            ServerTransaction serverTransaction) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
    
    public ActivityContextInterface getActivityContextInterface(
            Dialog dialog) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
}
