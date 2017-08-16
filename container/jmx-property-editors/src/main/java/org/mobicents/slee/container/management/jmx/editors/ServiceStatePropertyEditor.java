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

import javax.slee.management.ServiceState;

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

