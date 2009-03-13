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
