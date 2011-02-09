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

