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

import javax.slee.ServiceID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.SbbEntityIDPropertyEditor;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.sbbentity.NonRootSbbEntityID;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntityID;

public class SbbEntityIDPropertyEditorTest {

	private SbbEntityIDPropertyEditor propertyEditor = new SbbEntityIDPropertyEditor();

	private void testGetAsText(SbbEntityID object) throws Exception {
		propertyEditor.setValue(object);
		Assert.assertEquals(object.toString(), propertyEditor.getAsText());
	}

	private void testGetValue(SbbEntityID object) throws Exception {
		propertyEditor.setAsText(object.toString());
		Assert.assertEquals(object, propertyEditor.getValue());
	}

	@Test 
	public void testGetAsTextSBBEntityID1() throws Exception {
		ServiceID sid = new ServiceID("xxx", "yyy", "1.0");
    	RootSbbEntityID rsid = new RootSbbEntityID(sid, "rootConvergence");
		testGetAsText(rsid);
	}
	@Test
	public void testGetAsTextSBBEntityID2() throws Exception {
		ServiceID sid = new ServiceID("xxx", "yyy", "1.0");
    	SbbEntityID sbbEntityID = new RootSbbEntityID(sid, "rootConvergence");
    	sbbEntityID = new NonRootSbbEntityID(sbbEntityID, "relationX", "childX");
    	sbbEntityID = new NonRootSbbEntityID(sbbEntityID, "relationY", "childY");
		testGetAsText(sbbEntityID);
	}

	@Test
	public void testGetValueSBBEntityID1() throws Exception {
		ServiceID sid = new ServiceID("xxx", "yyy", "1.0");
    	RootSbbEntityID rsid = new RootSbbEntityID(sid, "rootConvergence");
		testGetValue(rsid);
	}
	
	@Test
	public void testGetValueSBBEntityID2() throws Exception {
		ServiceID sid = new ServiceID("xxx", "yyy", "1.0");
		SbbEntityID sbbEntityID = new RootSbbEntityID(sid, "rootConvergence");
    	sbbEntityID = new NonRootSbbEntityID(sbbEntityID, "relationX", "childX");
    	sbbEntityID = new NonRootSbbEntityID(sbbEntityID, "relationY", "childY");
		testGetValue(sbbEntityID);
	}

}