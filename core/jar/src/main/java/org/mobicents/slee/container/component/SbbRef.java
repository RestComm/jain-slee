/*
 * SbbRef.java
 * 
 * Created on Aug 12, 2004
 * 
 * Author: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component;

import java.io.Serializable;

/**
 *SSBBRef -- This is used in child relation.
 *
 *@author M. Ranganathan
 *
 */
public class SbbRef implements Serializable {
    private ComponentKey componentKey;
    private String alias;
    public SbbRef( ComponentKey componentKey, String alias	) {
        this.componentKey = componentKey;
        this.alias = alias;
    }
    public ComponentKey getComponentKey() {
        return this.componentKey;
    }
    public String getAlias() {
        return this.alias;
    }

}
