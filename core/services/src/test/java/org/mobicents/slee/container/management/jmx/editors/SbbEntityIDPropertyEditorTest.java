package org.mobicents.slee.container.management.jmx.editors;
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