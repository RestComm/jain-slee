package org.mobicents.slee.container.management.jmx.editors.test;

import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.management.LibraryID;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDArrayPropertyEditor;

public class ComponentIDArrayPropertyEditorTest { 
	
	@Test
	public void testEditorRoundtrip() throws Exception {
		ComponentID[] componentIDs = { new LibraryID("name","vendor","version") , new SbbID("name","vendor","version") };
		ComponentIDArrayPropertyEditor propertyEditor1 = new ComponentIDArrayPropertyEditor();
		propertyEditor1.setValue(componentIDs);
		String text = propertyEditor1.getAsText();
		ComponentIDArrayPropertyEditor propertyEditor2 = new ComponentIDArrayPropertyEditor();
		propertyEditor2.setAsText(text);
		ComponentID[] componentIDsCopy = (ComponentID[])propertyEditor2.getValue();
		Assert.assertEquals(componentIDs.length, componentIDsCopy.length);
		Assert.assertEquals(componentIDs[0], componentIDsCopy[0]);
		Assert.assertEquals(componentIDs[1], componentIDsCopy[1]);
	}
	
	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			ComponentIDArrayPropertyEditor propertyEditor = new ComponentIDArrayPropertyEditor();
			propertyEditor.setAsText("SbbID(name=name,vendor=vendor,version=version);ZeCarlosComponent(name=name,vendor=vendor,version=version)");
			Assert.fail("editor allowed setting invalid component id string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
}

