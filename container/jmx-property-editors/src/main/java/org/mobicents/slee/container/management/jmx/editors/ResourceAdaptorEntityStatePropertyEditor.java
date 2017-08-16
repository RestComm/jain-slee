/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.jmx.editors;


import javax.slee.management.ResourceAdaptorEntityState;

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

