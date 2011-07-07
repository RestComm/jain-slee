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

import javax.slee.ServiceID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.SBBEntityIDArrayPropertyEditor;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.sbbentity.NonRootSbbEntityID;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntityID;

public class SBBEntityIDArrayPropertyEditorTest { 
	
	@Test
	public void testEditorRoundtrip() throws Exception {
		
		ServiceID sid = new ServiceID("xxx", "yyy", "1.0");
    	RootSbbEntityID rsid = new RootSbbEntityID(sid, "rootConvergence");
    	NonRootSbbEntityID nrsid = new NonRootSbbEntityID(rsid, "relationX", "childY");

		SbbEntityID[] sbbEntityIDs = { rsid , nrsid };
		SBBEntityIDArrayPropertyEditor propertyEditor1 = new SBBEntityIDArrayPropertyEditor();
		propertyEditor1.setValue(sbbEntityIDs);
		String text = propertyEditor1.getAsText();
		SBBEntityIDArrayPropertyEditor propertyEditor2 = new SBBEntityIDArrayPropertyEditor();
		propertyEditor2.setAsText(text);
		SbbEntityID[] sbbEntityIDsCopy = (SbbEntityID[])propertyEditor2.getValue();
		Assert.assertEquals(sbbEntityIDs.length, sbbEntityIDsCopy.length);
		Assert.assertEquals(sbbEntityIDs[0], sbbEntityIDsCopy[0]);
		Assert.assertEquals(sbbEntityIDs[1], sbbEntityIDsCopy[1]);
	}

	@Test
	public void testGetValueInvalid1() throws Exception {
		try {
			SBBEntityIDArrayPropertyEditor propertyEditor = new SBBEntityIDArrayPropertyEditor();
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
			SBBEntityIDArrayPropertyEditor propertyEditor = new SBBEntityIDArrayPropertyEditor();
			propertyEditor.setAsText("/ServiceID[name=xxx,vendor=yyy,version=1.0]/convergenceNAme;/ServiceID[name=xxx,vendor=yyy,version=1.0]/rootConvergence/relationX");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
}

