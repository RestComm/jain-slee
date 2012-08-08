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
