/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.facilities.Level;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

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

