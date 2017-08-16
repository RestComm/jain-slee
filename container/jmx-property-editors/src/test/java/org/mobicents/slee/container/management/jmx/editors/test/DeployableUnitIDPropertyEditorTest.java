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

package org.mobicents.slee.container.management.jmx.editors.test;

import javax.slee.management.DeployableUnitID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.DeployableUnitIDPropertyEditor;

/**
 * @author martins
 *
 */
public class DeployableUnitIDPropertyEditorTest {
    
	private DeployableUnitIDPropertyEditor propertyEditor = new DeployableUnitIDPropertyEditor();

	
	@Test
	public void testGetAsTextServiceID() throws Exception {
		DeployableUnitID duID = new DeployableUnitID("url");
		propertyEditor.setValue(duID);
		Assert.assertEquals(duID.toString(), propertyEditor.getAsText());		
	}
	
	@Test
	public void testGetValueServiceID() throws Exception {
		DeployableUnitID duID = new DeployableUnitID("url");
		propertyEditor.setAsText(duID.toString());
		Assert.assertEquals(duID, propertyEditor.getValue());		
	}
	
	@Test
	public void testGetValueInvalidID() throws Exception {
		try {
			propertyEditor.setAsText("DeployableUnitID[URL=url]");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
	
}

