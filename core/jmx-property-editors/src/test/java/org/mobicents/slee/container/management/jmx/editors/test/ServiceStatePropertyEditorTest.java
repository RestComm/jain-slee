package org.mobicents.slee.container.management.jmx.editors.test;

import javax.slee.management.ServiceState;

import org.jboss.util.propertyeditor.TextPropertyEditorSupport;
import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.ServiceStatePropertyEditor;

/**
 * 
 * @author martins
 * 
 */
public class ServiceStatePropertyEditorTest extends TextPropertyEditorSupport {

	private ServiceStatePropertyEditor propertyEditor = new ServiceStatePropertyEditor();

	private void testGetAsText(ServiceState state) throws Exception {
		propertyEditor.setValue(state);
		Assert.assertEquals(state.toString(), propertyEditor.getAsText());
	}

	private void testGetValue(ServiceState state) throws Exception {
		propertyEditor.setAsText(state.toString());
		Assert.assertEquals(state, propertyEditor.getValue());
	}

	@Test
	public void testGetAsTextINACTIVE() throws Exception {
		testGetAsText(ServiceState.INACTIVE);
	}

	@Test
	public void testGetValueINACTIVE() throws Exception {
		testGetValue(ServiceState.INACTIVE);
	}

	@Test
	public void testGetAsTextACTIVE() throws Exception {
		testGetAsText(ServiceState.ACTIVE);
	}

	@Test
	public void testGetValueACTIVE() throws Exception {
		testGetValue(ServiceState.ACTIVE);
	}

	@Test
	public void testGetAsTextSTOPPPING() throws Exception {
		testGetAsText(ServiceState.STOPPING);
	}

	@Test
	public void testGetValueSTOPPPING() throws Exception {
		testGetValue(ServiceState.STOPPING);
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
