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
