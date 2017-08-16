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

import javax.slee.facilities.Level;

/**
 * Property Editor for the Level object.
 * 
 * @author M. Ranganathan
 *
 */
public class LevelPropertyEditor extends TextPropertyEditorSupport {
    
    public void setAsText( String level ) {
        if(level.equalsIgnoreCase(Level.FINE.toString()))
            super.setValue(Level.FINE);
        else
        if(level.equalsIgnoreCase(Level.FINER.toString()))
            super.setValue(Level.FINER);
        else
        if(level.equalsIgnoreCase(Level.OFF.toString()))
            super.setValue(Level.OFF);
        else
        if(level.equalsIgnoreCase(Level.FINEST.toString()))
            super.setValue(Level.FINEST);
        else
        if(level.equalsIgnoreCase(Level.INFO.toString()))
            super.setValue(Level.INFO);
        else
        if(level.equalsIgnoreCase(Level.WARNING.toString()))
            super.setValue(Level.WARNING);
        else
        if(level.equalsIgnoreCase(Level.SEVERE.toString()))
            super.setValue(Level.SEVERE);
        else
            if(level.equalsIgnoreCase(Level.CONFIG.toString()))
                super.setValue(Level.CONFIG);
        else
            throw new IllegalArgumentException(" Bad level " + level);

    }
    

}

