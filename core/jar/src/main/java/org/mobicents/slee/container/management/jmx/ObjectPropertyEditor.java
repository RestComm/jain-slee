/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ObjectEditor.java
 * 
 * Created on 13 déc. 2004
 *
 */
package org.mobicents.slee.container.management.jmx;

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
