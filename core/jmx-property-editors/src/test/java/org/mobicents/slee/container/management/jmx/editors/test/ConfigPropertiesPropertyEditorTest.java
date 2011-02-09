package org.mobicents.slee.container.management.jmx.editors.test;

import javax.slee.resource.ConfigProperties;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.ConfigPropertiesPropertyEditor;

/**
 * 
 * @author martins
 *
 */
public class ConfigPropertiesPropertyEditorTest {
   
	private ConfigPropertiesPropertyEditor propertyEditor = new ConfigPropertiesPropertyEditor();
	
	@Test
	public void testGetAsText() throws Exception {
		ConfigProperties configProperties = new ConfigProperties();
		configProperties.addProperty(new ConfigProperties.Property("p1","java.lang.String",ConfigProperties.Property.toObject("java.lang.String", "string")));
		configProperties.addProperty(new ConfigProperties.Property("p2","java.lang.Integer",ConfigProperties.Property.toObject("java.lang.Integer", "0")));
		configProperties.addProperty(new ConfigProperties.Property("p3","java.lang.String",null));
		propertyEditor.setValue(configProperties);
		Assert.assertEquals(configProperties.toString(), propertyEditor.getAsText());		
	}
	
	@Test
	public void testGetValue() throws Exception {
		ConfigProperties configProperties = new ConfigProperties();
		configProperties.addProperty(new ConfigProperties.Property("p1","java.lang.String",ConfigProperties.Property.toObject("java.lang.String", "string")));
		configProperties.addProperty(new ConfigProperties.Property("p2","java.lang.Integer",ConfigProperties.Property.toObject("java.lang.Integer", "0")));
		configProperties.addProperty(new ConfigProperties.Property("p3","java.lang.String",null));
		propertyEditor.setAsText(configProperties.toString());
		ConfigProperties anotherConfigProperties = (ConfigProperties) propertyEditor.getValue();
		Assert.assertEquals(configProperties.getProperties().length, anotherConfigProperties.getProperties().length);
		Assert.assertEquals(configProperties.getProperty("p1"), anotherConfigProperties.getProperty("p1"));
		Assert.assertEquals(configProperties.getProperty("p2"), anotherConfigProperties.getProperty("p2"));
		Assert.assertEquals(configProperties.getProperty("p3"), anotherConfigProperties.getProperty("p3"));
	}
	
	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			propertyEditor.setAsText("[(name=type:value)");
			Assert.fail("editor allowed setting invalid properties string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}
}
