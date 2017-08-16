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

import javax.slee.ComponentID;

/**
 *Property editor for component ID array.
 *
 */
public class ComponentIDArrayPropertyEditor
		extends TextPropertyEditorSupport {
    
	public final String CID_SEPARATOR = ";";
	
    public String getAsText( ) {
        ComponentID[] componentIds = (ComponentID[]) this.getValue();
        if ( componentIds == null) return "null";
        else {
            StringBuffer sb = new StringBuffer();
            for ( int i = 0; i < componentIds.length; i++) {
                sb.append(componentIds[i].toString());
                if (i < componentIds.length-1) {
                	sb.append(CID_SEPARATOR);
                }
            }
            return sb.toString();
        }
    }
    
    /**
     * Set the element as text value, parse it and setValue.
     * The separator is CID_SEPARATOR 
     */
    public void setAsText(String text ) {
        if ( text == null || text.equals("")) {
            super.setValue( new ComponentID[0]);
        } else {
            java.util.ArrayList results = new java.util.ArrayList();
            // the format for component ID is name vendor version.
            java.util.StringTokenizer st = new java.util.StringTokenizer(text,CID_SEPARATOR,true);
            ComponentIDPropertyEditor cidPropEditor = new ComponentIDPropertyEditor();
            while (st.hasMoreTokens()) {
                cidPropEditor.setAsText(st.nextToken());
                if (st.hasMoreTokens()) {
                	st.nextToken();
                }
                results.add(cidPropEditor.getValue());
            }
            ComponentID[] cid = new ComponentID[results.size()];
            results.toArray(cid);
            this.setValue(cid);
        }
        
    }

}

