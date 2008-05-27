/*
 * MobicentsResourceAdaptorTypeBinding.java
 * 
 * Created on Nov 4, 2004
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

package org.mobicents.slee.container.component;

import java.io.Serializable;
import java.util.HashSet;

import javax.slee.resource.ResourceAdaptorTypeDescriptor;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 *Interface defining the resource adapter type binding element that
 *appears in the SBB deployment deescriptor
 */
public interface MobicentsResourceAdaptorTypeBinding extends Serializable {
   
    

    /**
     * @return Returns the activityContextInterfaceFactoryName.
     */
    public String getActivityContextInterfaceFactoryName() ;
   
    /**
     * @return Returns the description.
     */
    public String getDescription() ;
    
    /**
     * @return Returns the resourceAdapterEntityBindings.
     */
    public HashSet getResourceAdapterEntityBindings() ;
   
    /**
     * @return Returns the resourceAdapterTypeId.
     */
    public ResourceAdaptorTypeID getResourceAdapterTypeId() ;
   
}

