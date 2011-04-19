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

import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.management.LibraryID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDArrayPropertyEditor;

public class ComponentIDArrayPropertyEditorTest { 
	
	@Test
	public void testEditorRoundtrip() throws Exception {
		ComponentID[] componentIDs = { new LibraryID("name","vendor","version") , new SbbID("name","vendor","version") };
		ComponentIDArrayPropertyEditor propertyEditor1 = new ComponentIDArrayPropertyEditor();
		propertyEditor1.setValue(componentIDs);
		String text = propertyEditor1.getAsText();
		ComponentIDArrayPropertyEditor propertyEditor2 = new ComponentIDArrayPropertyEditor();
		propertyEditor2.setAsText(text);
		ComponentID[] componentIDsCopy = (ComponentID[])propertyEditor2.getValue();
		Assert.assertEquals(componentIDs.length, componentIDsCopy.length);
		Assert.assertEquals(componentIDs[0], componentIDsCopy[0]);
		Assert.assertEquals(componentIDs[1], componentIDsCopy[1]);
	}
	
	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			ComponentIDArrayPropertyEditor propertyEditor = new ComponentIDArrayPropertyEditor();
			propertyEditor.setAsText("SbbID(name=name,vendor=vendor,version=version);ZeCarlosComponent(name=name,vendor=vendor,version=version)");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
}

