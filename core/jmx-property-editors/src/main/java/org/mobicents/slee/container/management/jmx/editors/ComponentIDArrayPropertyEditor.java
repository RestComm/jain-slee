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

