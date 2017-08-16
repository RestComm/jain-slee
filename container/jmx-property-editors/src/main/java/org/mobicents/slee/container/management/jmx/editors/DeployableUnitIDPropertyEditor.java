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

import javax.slee.management.DeployableUnitID;

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

