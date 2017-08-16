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
import org.mobicents.slee.container.management.jmx.editors.SbbEntityIDArrayPropertyEditor;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.sbbentity.NonRootSbbEntityID;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntityID;

public class SbbEntityIDArrayPropertyEditorTest { 
	
	@Test
	public void testEditorRoundtrip() throws Exception {
		
		ServiceID sid = new ServiceID("xxx", "yyy", "1.0");
    	RootSbbEntityID rsid = new RootSbbEntityID(sid, "rootConvergence");
    	NonRootSbbEntityID nrsid = new NonRootSbbEntityID(rsid, "relationX", "childY");

		SbbEntityID[] sbbEntityIDs = { rsid , nrsid };
		SbbEntityIDArrayPropertyEditor propertyEditor1 = new SbbEntityIDArrayPropertyEditor();
		propertyEditor1.setValue(sbbEntityIDs);
		String text = propertyEditor1.getAsText();
		SbbEntityIDArrayPropertyEditor propertyEditor2 = new SbbEntityIDArrayPropertyEditor();
		propertyEditor2.setAsText(text);
		SbbEntityID[] sbbEntityIDsCopy = (SbbEntityID[])propertyEditor2.getValue();
		Assert.assertEquals(sbbEntityIDs.length, sbbEntityIDsCopy.length);
		Assert.assertEquals(sbbEntityIDs[0], sbbEntityIDsCopy[0]);
		Assert.assertEquals(sbbEntityIDs[1], sbbEntityIDsCopy[1]);
	}

	@Test 
	public void testGetValueInvalid1() throws Exception {
		try {
			SbbEntityIDArrayPropertyEditor propertyEditor = new SbbEntityIDArrayPropertyEditor();
			propertyEditor.setAsText("/ServiceID[name=xxx,vendor=yyy,version=1.0];/ServiceID[name=xxx,vendor=yyy,version=1.0]/rootConvergence/relationX/childY");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
	
	@Test
	public void testGetValueInvalid2() throws Exception {
		try {
			SbbEntityIDArrayPropertyEditor propertyEditor = new SbbEntityIDArrayPropertyEditor();
			propertyEditor.setAsText("/ServiceID[name=xxx,vendor=yyy,version=1.0]/convergenceNAme;/ServiceID[name=xxx,vendor=yyy,version=1.0]/rootConvergence/relationX");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
}

