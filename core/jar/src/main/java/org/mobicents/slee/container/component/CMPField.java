
/*
* The Open SLEE project.
*
* The source code contained in this file is in in the public domain.          
* It can be used in any project, or product without prior permission, 	      
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/

package org.mobicents.slee.container.component;

import java.io.Serializable;

/**
 * A representation class for the <cmp-field> node element of the sbb deployment
 * descriptor.
 *
 * @author Emil Ivov
 */
public class CMPField implements Serializable{

    private ComponentKey sbbComponentKey             = null;
    private String       fieldName                   = null;

    /**
     * Creates a CMP field with the specified name that references the SBB
     * represented by the specified <code>sbbComponentKey</code>
     * @param fieldName the name of the CMP field
     * @param sbbComponentKey the component key of the of the sbb referenced by
     * the cmp field.
     */
    public CMPField(String fieldName, ComponentKey sbbComponentKey)
    {
        this.fieldName       = fieldName;
        this.sbbComponentKey = sbbComponentKey;
    }

    /**
     * Returns a reference to the ComponentKey of the SBB pointed to by the
     * <sbb-alias-ref> node of the cmp-feld.
     * @return ComponentKey
     */
    public ComponentKey getSbbComponentKey()
    {
        return sbbComponentKey;
    }

    /**
     * Returns the name of this CMP field
     * @return a String containing the field's name.
     */
    public String getFieldName() {
        return fieldName;
    }

}
