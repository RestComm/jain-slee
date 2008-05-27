/*
* Created on Aug 8, 2004
*
*The Open SLEE project
*
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*
*/

package org.mobicents.slee.container.component;

import java.io.Serializable;

import javax.slee.profile.ProfileSpecificationID;


/** This key is used to retrieve profile spec elements from the component table.
 * 
 * @author M. Ranganathan
 *
 */
public class ProfileSpecificationIDImpl  extends 
	ComponentIDImpl implements ProfileSpecificationID,Serializable {
	
    public ProfileSpecificationIDImpl (ComponentKey key) {
        super(key);
        super.componentType = PROFILE_SPECIFICATION_ID;
    }

   

    

}
