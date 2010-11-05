package org.mobicents.slee.container.management.jmx.editors;

import javax.slee.Address;
import javax.slee.AddressPlan;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;

/**
 * Property Editor for {@link Address}. 
 * 
 * @author martins
 * 
 */
public class AddressPropertyEditor extends TextPropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {
        try {
            int delimerSize = 2;
        	int delimiter = text.indexOf(": ");
            if (delimiter == -1) {
            	delimerSize = 1;
            	delimiter = text.indexOf(":");
            }
            if (delimiter == -1) {
            	throw new IllegalArgumentException("text arg should be \"address plan as string\" + \": \" + \"address as string\"");
            }
        	String addressPlan = text.substring(0,delimiter);
        	String address = text.substring(delimiter+delimerSize);
            this.setValue(new Address(AddressPlan.fromString(addressPlan),address));               
        } catch (Throwable ex) {
            throw new IllegalArgumentException(ex.getMessage(),ex);
        }
    }

}