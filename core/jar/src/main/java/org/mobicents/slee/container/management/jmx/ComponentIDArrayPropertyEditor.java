/*
 * ComponentIDArrayPropertyEditor.java
 * 
 * Created on Nov 23, 2004
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

import org.mobicents.slee.container.component.ComponentIDImpl;

import javax.slee.ComponentID;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 *Propoerty editor for component ID array.
 *
 */
public class ComponentIDArrayPropertyEditor
		extends TextPropertyEditorSupport {
    
    public String getAsText( ) {
        ComponentIDImpl[] componentIds = (ComponentIDImpl[]) this.getValue();
        if ( componentIds == null) return "null";
        else {
            StringBuffer sb = new StringBuffer();
            for ( int i = 0; i < componentIds.length; i++) {
                sb.append(componentIds[i].toString());
                sb.append("|");
            }
            return sb.toString();
        }
    }
    
    /**
     * Set the element as text value, parse it and setValue.
     * The separator is "|" (need to check this).
     */
    public void setAsText(String text ) {
        if ( text == null || text.equals("")) {
            super.setValue( new ComponentIDImpl[0]);
        } else {
            java.util.ArrayList results = new java.util.ArrayList();
            // the format for component ID is name vendor version.
            java.util.StringTokenizer st = new java.util.StringTokenizer(text,"|",true);
            ComponentIDPropertyEditor cidPropEditor = new ComponentIDPropertyEditor();
            while (st.hasMoreTokens()) {
                cidPropEditor.setAsText(st.nextToken());
                st.nextToken();
                results.add(cidPropEditor.getValue());
            }
            ComponentIDImpl[] cid = new ComponentIDImpl[results.size()];
            results.toArray(cid);
            this.setValue(cid);
        }
        
    }

}

