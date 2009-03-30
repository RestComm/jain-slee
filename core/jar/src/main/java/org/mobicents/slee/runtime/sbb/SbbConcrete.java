/*
 * SbbConcrete.java
 * 
 * Created on Dec 24, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
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

package org.mobicents.slee.runtime.sbb;

import javax.slee.Sbb;

import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * An implementation interface for sbbs. Additonal methods are added to store the runtime state.
 */
public interface SbbConcrete extends Sbb {
    
    public SbbEntity getSbbEntity();
    
    public void setSbbEntity(SbbEntity sbbEntity);
    
    public void sbbSetActivityContextInterface(Object aci );
    
}

