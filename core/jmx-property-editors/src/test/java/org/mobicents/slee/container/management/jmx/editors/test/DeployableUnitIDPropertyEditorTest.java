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

