/*
 * ServiceStatePropertyEditor.java
 * 
 * Created on Feb 18, 2005
 * 
 * Created by: B. Bright
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

package org.mobicents.slee.container.management.jmx;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;

import javax.slee.management.ServiceState;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for the ServiceState object.
 * 
 * @author B. Bright
 *
 */
public class ServiceStatePropertyEditor extends TextPropertyEditorSupport {
    
    public void setAsText( String state ) {
        if(state.equalsIgnoreCase(ServiceState.ACTIVE.toString()))
            super.setValue(ServiceState.ACTIVE);
        else
        if(state.equalsIgnoreCase("active"))
            super.setValue(ServiceState.ACTIVE);
        else
        if(state.equalsIgnoreCase(ServiceState.INACTIVE.toString()))
            super.setValue(ServiceState.INACTIVE);
        else
        if(state.equalsIgnoreCase("inactive"))
            super.setValue(ServiceState.INACTIVE);
        else
            throw new IllegalArgumentException(" Bad Service State " + state);

    }
    

}

