package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.facilities.TraceLevel;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

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

