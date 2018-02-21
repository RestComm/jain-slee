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
            if(level.equalsIgnoreCase(TraceLevel.CONFIG_STRING))
                super.setValue(TraceLevel.CONFIG);
        else
            throw new IllegalArgumentException(" Bad level " + level);

    }
    

}

