package org.mobicents.slee.container.management.jmx.editors;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 */
public class ObjectPropertyEditor 
	   extends TextPropertyEditorSupport {
    
    public void setAsText(String attributeValue){
        super.setValue(attributeValue);
    }
}
