/*
 * Created on Feb 2, 2005
 * 
 * The Mobicents Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.runtime;

/**
 * An Implementation interface. This is implemented by
 * both the generic ActivityContextInterface as well
 * as the SbbActivityContextInterface. It is just here
 * to make the implementation more streamlined.
 * 
 * @author F.Moggia
 */
public interface ActivityContextIDInterface {
    public String retrieveActivityContextID();
    public ActivityContext retrieveActivityContext();
}
