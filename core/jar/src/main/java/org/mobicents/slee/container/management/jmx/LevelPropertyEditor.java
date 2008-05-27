/*
 * LevelPropertyEditor.java
 * 
 * Created on Oct 27, 2004
 * 
 * Created by: M. Ranganathan
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
            throw new IllegalArgumentException(" Bad level " + level);

    }
    

}

