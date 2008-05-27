/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileSpecificationIDPropertyEditor.java
 * 
 * Created on Nov 14, 2004
 *
 */
package org.mobicents.slee.container.profile;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class ProfileSpecificationIDPropertyEditor extends
        TextPropertyEditorSupport {

    
    public void setAsText( String profileSpecificationID) {
        ComponentKey componentKey=new ComponentKey(profileSpecificationID);               
        super.setValue(new ProfileSpecificationIDImpl(componentKey));
    }

}
