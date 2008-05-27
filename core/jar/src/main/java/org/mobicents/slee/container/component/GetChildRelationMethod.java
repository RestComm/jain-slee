/*
* The source code contained in this file is in in the public domain.          
* It can be used in any project or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*
*/

package org.mobicents.slee.container.component;

import javax.slee.SbbID;

/**
 * Represents the get-child-relation-method node of the sbb 
 * deployment descriptor and its properties
 *
 * @author Emil Ivov
 */

public class GetChildRelationMethod {
    private SbbID sbbID = null;
    private String name = null;
    private byte defaultPriority = 0;

    /**
     * Creates a GetChildRelationMethod from the specified child sbb component
     * key, method name and priority
     * @param childKey the ComponentKey of the child sbb
     * @param name the name of the method
     * @param defaultPriority a default priority of the child sbb relative to
     * its siblings
     */
    public GetChildRelationMethod(SbbID sbbID,
                                  String name,
                                  byte defaultPriority) {
        this.sbbID = sbbID;
        this.name = name;
        this.defaultPriority = defaultPriority;
    }

    /**
     * Retrieves the SbbID of the child sbb.
     * @return 
     */
    public SbbID getSbbID(){
        return sbbID;
    }

    /**
     * Returns the name of the method represented by this object.
     * @return String
     */
    public String getMethodName(){
        return name;
    }

    /**
     * Returns the default priority of the child sbb relative to its siblings.
     * @return the default priority of the child sbb relative to its siblings.
     */
    public byte getDefaultPriority(){
        return defaultPriority;
    }

}
