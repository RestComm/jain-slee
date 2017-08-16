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

import javax.slee.facilities.TraceLevel;

/**
 * Property Editor for the TraceLevel object.
 * 
 * @author martins
 *
 */
public class TraceLevelPropertyEditor extends TextPropertyEditorSupport {
    
    public void setAsText( String level ) {
        if(level.equalsIgnoreCase(TraceLevel.FINE.toString()))
            super.setValue(TraceLevel.FINE);
        else
        if(level.equalsIgnoreCase(TraceLevel.FINER.toString()))
            super.setValue(TraceLevel.FINER);
        else
        if(level.equalsIgnoreCase(TraceLevel.OFF.toString()))
            super.setValue(TraceLevel.OFF);
        else
        if(level.equalsIgnoreCase(TraceLevel.FINEST.toString()))
            super.setValue(TraceLevel.FINEST);
        else
        if(level.equalsIgnoreCase(TraceLevel.INFO.toString()))
            super.setValue(TraceLevel.INFO);
        else
        if(level.equalsIgnoreCase(TraceLevel.WARNING.toString()))
            super.setValue(TraceLevel.WARNING);
        else
        if(level.equalsIgnoreCase(TraceLevel.SEVERE.toString()))
            super.setValue(TraceLevel.SEVERE);
        else
            if(level.equalsIgnoreCase(TraceLevel.CONFIG_STRING.toString()))
                super.setValue(TraceLevel.CONFIG);
        else
            throw new IllegalArgumentException(" Bad level " + level);

    }
    

}

