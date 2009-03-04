/*
 * InstalledUsageParameterSet.java
 * 
 * Created on Jan 12, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
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

package org.mobicents.slee.container.management.jmx;

import javax.slee.SbbID;
import javax.slee.ServiceID;


/**
 *@author M. Ranganathan
 *
 */
public interface InstalledUsageParameterSet {

    	public SbbID getSbbID();
    	public ServiceID getServiceID();
    	public String getName();
    	public void reset();
    	public void setName( String name );
    	public void setSbbUsageMBean (SbbUsageMBeanImpl usageParameterMBean );
    	public SbbUsageMBeanImpl getSbbUsageMBean();
}

