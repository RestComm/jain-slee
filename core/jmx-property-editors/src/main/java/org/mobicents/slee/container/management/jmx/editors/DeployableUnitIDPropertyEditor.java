package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.management.DeployableUnitID;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property editor for {@link DeployableUnitID}
 * @author martins
 *
 */
public class DeployableUnitIDPropertyEditor extends TextPropertyEditorSupport {
    
	public void setAsText(String text) throws IllegalArgumentException {
        try {
        	String prefix = "DeployableUnitID[url=";
            if (text.startsWith(prefix) && text.charAt(text.length()-1) == ']') {
            	this.setValue(new DeployableUnitID(text.substring(prefix.length(),text.length()-1)));
            	return;
            }
            throw new IllegalArgumentException("Must be DeployableUnitID[url=X] where X is the du original URL");
        } catch (Throwable ex) {
            throw new IllegalArgumentException("Must be DeployableUnitID[url=X] where X is the du original URL",ex);
        }
    }
	
}

