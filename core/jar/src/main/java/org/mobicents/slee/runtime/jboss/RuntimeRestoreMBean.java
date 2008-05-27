/*
 * RuntimeRestoreMBean.java
 * 
 * Created on Aug 16, 2005
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

package org.mobicents.slee.runtime.jboss;

import javax.management.ObjectName;

/**
 *An interface for the restore service. When a hot standby
 *comes to life, the container will start up the specified 
 *method of this service.
 *
 */
public interface  RuntimeRestoreMBean  {
    
    /**
     * Restore all the installed timers and get them running.
     * @throws Exception
     *
     */
    public void restoreRuntimeState() throws Exception;
    
    
    public void setRuntimeRestoreMBean(ObjectName objectName);
    
    public void saveRuntimeState() throws Exception;

}

