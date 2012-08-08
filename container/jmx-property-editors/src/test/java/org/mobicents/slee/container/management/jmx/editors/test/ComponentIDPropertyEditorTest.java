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
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;

public class ComponentIDPropertyEditorTest {

	private ComponentIDPropertyEditor propertyEditor = new ComponentIDPropertyEditor();

	private void testGetAsText(ComponentID object) throws Exception {
		propertyEditor.setValue(object);
		Assert.assertEquals(object.toString(), propertyEditor.getAsText());
	}

	private void testGetValue(ComponentID object) throws Exception {
		propertyEditor.setAsText(object.toString());
		Assert.assertEquals(object, propertyEditor.getValue());
	}

	@Test
	public void testGetAsTextLibraryID() throws Exception {
		testGetAsText(new LibraryID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueLibraryID() throws Exception {
		testGetValue(new LibraryID("name", "vendor", "version"));
	}

	@Test
	public void testGetAsTextEventTypeID() throws Exception {
		testGetAsText(new EventTypeID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueEventTypeID() throws Exception {
		testGetValue(new EventTypeID("name", "vendor", "version"));
	}

	@Test
	public void testGetAsTextProfileSpecificationID() throws Exception {
		testGetAsText(new ProfileSpecificationID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueProfileSpecificationID() throws Exception {
		testGetValue(new ProfileSpecificationID("name", "vendor", "version"));
	}

	@Test
	public void testGetAsTextResourceAdaptorID() throws Exception {
		testGetAsText(new ResourceAdaptorID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueResourceAdaptorID() throws Exception {
		testGetValue(new ResourceAdaptorID("name", "vendor", "version"));
	}

	@Test
	public void testGetAsTextResourceAdaptorTypeID() throws Exception {
		testGetAsText(new ResourceAdaptorTypeID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueResourceAdaptorTypeID() throws Exception {
		testGetValue(new ResourceAdaptorTypeID("name", "vendor", "version"));
	}

	@Test
	public void testGetAsTextSbbID() throws Exception {
		testGetAsText(new SbbID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueSbbID() throws Exception {
		testGetValue(new SbbID("name", "vendor", "version"));
	}

	@Test
	public void testGetAsTextServiceID() throws Exception {
		testGetAsText(new ServiceID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueServiceID() throws Exception {
		testGetValue(new ServiceID("name", "vendor", "version"));
	}

	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			propertyEditor
					.setAsText("ZeCarlosComponent(name=name,vendor=vendor,version=version)");
			Assert.fail("editor allowed setting invalid string "
					+ propertyEditor.getAsText());
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}