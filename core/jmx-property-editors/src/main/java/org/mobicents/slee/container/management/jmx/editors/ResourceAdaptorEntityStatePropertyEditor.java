package org.mobicents.slee.container.management.jmx.editors;


import javax.slee.management.ResourceAdaptorEntityState;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for the ResourceAdaptorEntityState object.
 * 
 * @author martins
 *
 */
public class ResourceAdaptorEntityStatePropertyEditor extends TextPropertyEditorSupport {
    
    public void setAsText( String state ) {
        if(state.equalsIgnoreCase(ResourceAdaptorEntityState.ACTIVE.toString()))
            super.setValue(ResourceAdaptorEntityState.ACTIVE);
        else
        if(state.equalsIgnoreCase(ResourceAdaptorEntityState.ACTIVE_STRING))
            super.setValue(ResourceAdaptorEntityState.ACTIVE);
        else
        if(state.equalsIgnoreCase(ResourceAdaptorEntityState.INACTIVE.toString()))
            super.setValue(ResourceAdaptorEntityState.INACTIVE);
        else
        if(state.equalsIgnoreCase(ResourceAdaptorEntityState.INACTIVE_STRING))
            super.setValue(ResourceAdaptorEntityState.INACTIVE);
        else
        if(state.equalsIgnoreCase(ResourceAdaptorEntityState.STOPPING.toString()))
            super.setValue(ResourceAdaptorEntityState.STOPPING);
        else
        if(state.equalsIgnoreCase(ResourceAdaptorEntityState.STOPPING_STRING))
                super.setValue(ResourceAdaptorEntityState.STOPPING);
        else
            throw new IllegalArgumentException("Bad Service State " + state);

    }
    

}

