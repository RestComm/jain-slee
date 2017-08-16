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

import javax.slee.Address;
import javax.slee.AddressPlan;

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