package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.management.ServiceState;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for the ServiceState object.
 * 
 * @author B. Bright
 * @author martins
 *
 */
public class ServiceStatePropertyEditor extends TextPropertyEditorSupport {
    
    public void setAsText( String state ) {
        if(state.equalsIgnoreCase(ServiceState.ACTIVE.toString()))
            super.setValue(ServiceState.ACTIVE);
        else
        if(state.equalsIgnoreCase(ServiceState.ACTIVE_STRING))
            super.setValue(ServiceState.ACTIVE);
        else
        if(state.equalsIgnoreCase(ServiceState.INACTIVE.toString()))
            super.setValue(ServiceState.INACTIVE);
        else
        if(state.equalsIgnoreCase(ServiceState.INACTIVE_STRING))
            super.setValue(ServiceState.INACTIVE);
        else
            if(state.equalsIgnoreCase(ServiceState.STOPPING.toString()))
                super.setValue(ServiceState.STOPPING);
            else
            if(state.equalsIgnoreCase(ServiceState.STOPPING_STRING))
                super.setValue(ServiceState.STOPPING);
        else
            throw new IllegalArgumentException("Bad Service State " + state);

    }
    

}

