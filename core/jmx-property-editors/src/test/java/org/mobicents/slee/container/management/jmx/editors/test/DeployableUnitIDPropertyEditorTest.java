package org.mobicents.slee.container.management.jmx.editors.test;

import javax.slee.management.DeployableUnitID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.DeployableUnitIDPropertyEditor;

/**
 * @author martins
 *
 */
public class DeployableUnitIDPropertyEditorTest {
    
	private DeployableUnitIDPropertyEditor propertyEditor = new DeployableUnitIDPropertyEditor();

	
	@Test
	public void testGetAsTextServiceID() throws Exception {
		DeployableUnitID duID = new DeployableUnitID("url");
		propertyEditor.setValue(duID);
		Assert.assertEquals(duID.toString(), propertyEditor.getAsText());		
	}
	
	@Test
	public void testGetValueServiceID() throws Exception {
		DeployableUnitID duID = new DeployableUnitID("url");
		propertyEditor.setAsText(duID.toString());
		Assert.assertEquals(duID, propertyEditor.getValue());		
	}
	
	@Test
	public void testGetValueInvalidID() throws Exception {
		try {
			propertyEditor.setAsText("DeployableUnitID[URL=url]");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
	
}

