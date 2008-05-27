/*
 * Created on Jul 2, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */
package org.mobicents.slee.container.component;

import javax.slee.ServiceID;
import javax.slee.management.DeployableUnitID;

/**
 * 
 * Implements the ServiceID
 * 
 * @author F.Moggia
 * 
 * 
 */
public class ServiceIDImpl 
	extends ComponentIDImpl implements ServiceID  {
    public ServiceIDImpl(ComponentKey id) {
        super(id);
    }
    
    public ServiceIDImpl(String name, String vendor, String version) {
        super(new ComponentKey(name, vendor, version));
    }
}
