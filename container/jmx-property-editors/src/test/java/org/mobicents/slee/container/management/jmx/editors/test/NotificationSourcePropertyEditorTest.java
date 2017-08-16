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