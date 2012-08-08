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

import javax.slee.ComponentID;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

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

