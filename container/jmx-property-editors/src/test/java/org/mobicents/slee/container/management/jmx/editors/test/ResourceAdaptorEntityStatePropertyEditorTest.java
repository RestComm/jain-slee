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

import javax.slee.management.ResourceAdaptorEntityState;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.ResourceAdaptorEntityStatePropertyEditor;

/**
 * 
 * @author martins
 * 
 */
public class ResourceAdaptorEntityStatePropertyEditorTest {

	private ResourceAdaptorEntityStatePropertyEditor propertyEditor = new ResourceAdaptorEntityStatePropertyEditor();

	private void testGetAsText(ResourceAdaptorEntityState state)
			throws Exception {
		propertyEditor.setValue(state);
		Assert.assertEquals(state.toString(), propertyEditor.getAsText());
	}

	private void testGetValue(ResourceAdaptorEntityState state)
			throws Exception {
		propertyEditor.setAsText(state.toString());
		Assert.assertEquals(state, propertyEditor.getValue());
	}

	@Test
	public void testGetAsTextINACTIVE() throws Exception {
		testGetAsText(ResourceAdaptorEntityState.INACTIVE);
	}

	@Test
	public void testGetValueINACTIVE() throws Exception {
		testGetValue(ResourceAdaptorEntityState.INACTIVE);
	}

	@Test
	public void testGetAsTextACTIVE() throws Exception {
		testGetAsText(ResourceAdaptorEntityState.ACTIVE);
	}

	@Test
	public void testGetValueACTIVE() throws Exception {
		testGetValue(ResourceAdaptorEntityState.ACTIVE);
	}

	@Test
	public void testGetAsTextSTOPPPING() throws Exception {
		testGetAsText(ResourceAdaptorEntityState.STOPPING);
	}

	@Test
	public void testGetValueSTOPPPING() throws Exception {
		testGetValue(ResourceAdaptorEntityState.STOPPING);
	}

	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			propertyEditor.setAsText("STOPPED");
			Assert.fail("editor allowed invalid string "
					+ propertyEditor.getAsText());
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

}
