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