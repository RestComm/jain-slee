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
