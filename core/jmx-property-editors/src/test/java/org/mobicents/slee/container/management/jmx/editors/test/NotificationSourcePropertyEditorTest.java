package org.mobicents.slee.container.management.jmx.editors.test;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.NotificationSource;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.SbbNotification;
import javax.slee.management.SubsystemNotification;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.NotificationSourcePropertyEditor;

/**
 * 
 * @author martins
 * 
 */
public class NotificationSourcePropertyEditorTest {

	private NotificationSourcePropertyEditor propertyEditor = new NotificationSourcePropertyEditor();

	private void testGetAsText(NotificationSource ns) throws Exception {
		propertyEditor.setValue(ns);
		Assert.assertEquals(ns.toString(), propertyEditor.getAsText());
	}

	private void testGetValue(NotificationSource ns) throws Exception {
		propertyEditor.setAsText(ns.toString());
		Assert.assertEquals(ns, propertyEditor.getValue());
	}

	@Test
	public void testGetAsTextProfileTableNotification() throws Exception {
		testGetAsText(new ProfileTableNotification("table"));
	}

	@Test
	public void testGetValueProfileTableNotification() throws Exception {
		testGetValue(new ProfileTableNotification("table"));
	}

	@Test
	public void testGetAsTextResourceAdaptorEntityNotification()
			throws Exception {
		testGetAsText(new ResourceAdaptorEntityNotification("entity"));
	}

	@Test
	public void testGetValueResourceAdaptorEntityNotification()
			throws Exception {
		testGetValue(new ResourceAdaptorEntityNotification("entity"));
	}

	@Test
	public void testGetAsTextSbbNotification() throws Exception {
		testGetAsText(new SbbNotification(new ServiceID("name","vendor","version"),new SbbID("name","vendor","version")));
	}

	@Test
	public void testGetValueSbbNotification() throws Exception {
		testGetValue(new SbbNotification(new ServiceID("name","vendor","version"),new SbbID("name","vendor","version")));
	}

	@Test
	public void testGetAsTextSubsystemNotification() throws Exception {
		testGetAsText(new SubsystemNotification("subsystem"));
	}

	@Test
	public void testGetValueSubsystemNotification() throws Exception {
		testGetValue(new SubsystemNotification("subsystem"));
	}

	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			propertyEditor
					.setAsText("ZeCarlosNotification(name=Ze)");
			Assert.fail("editor allowed setting invalid string "
					+ propertyEditor.getAsText());
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}