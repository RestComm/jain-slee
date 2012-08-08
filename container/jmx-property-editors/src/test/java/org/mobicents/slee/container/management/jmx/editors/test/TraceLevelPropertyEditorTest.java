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

import javax.slee.facilities.TraceLevel;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.TraceLevelPropertyEditor;

/**
 * 
 * @author martins
 *
 */
public class TraceLevelPropertyEditorTest {
    
private TraceLevelPropertyEditor propertyEditor = new TraceLevelPropertyEditor();
	
	private void testGetAsText(TraceLevel level) throws Exception {
		propertyEditor.setValue(level);
		Assert.assertEquals(level.toString(), propertyEditor.getAsText());	
	}
	
	private void testGetValue(TraceLevel level) throws Exception {
		propertyEditor.setAsText(level.toString());
		Assert.assertEquals(level, propertyEditor.getValue());
	}
	
	@Test
	public void testGetAsTextFINE() throws Exception {
		testGetAsText(TraceLevel.FINE);	
	}
	
	@Test
	public void testGetValueFINE() throws Exception {
		testGetValue(TraceLevel.FINE);	
	}
	
	@Test
	public void testGetAsTextFINER() throws Exception {
		testGetAsText(TraceLevel.FINER);		
	}
	
	@Test
	public void testGetValueFINER() throws Exception {
		testGetValue(TraceLevel.FINER);			
	}
	
	@Test
	public void testGetAsTextOFF() throws Exception {
		testGetAsText(TraceLevel.OFF);		
	}
	
	@Test
	public void testGetValueOFF() throws Exception {
		testGetValue(TraceLevel.OFF);		
	}
	
	@Test
	public void testGetAsTextFINEST() throws Exception {
		testGetAsText(TraceLevel.FINEST);		
	}
	
	@Test
	public void testGetValueFINEST() throws Exception {
		testGetValue(TraceLevel.FINEST);		
	}
	
	@Test
	public void testGetAsTextINFO() throws Exception {
		testGetAsText(TraceLevel.INFO);		
	}
	
	@Test
	public void testGetValueINFO() throws Exception {
		testGetValue(TraceLevel.INFO);	
	}
	
	@Test
	public void testGetAsTextWARNING() throws Exception {
		testGetAsText(TraceLevel.WARNING);		
	}
	
	@Test
	public void testGetValueWARNING() throws Exception {
		testGetValue(TraceLevel.WARNING);		
	}
	
	@Test
	public void testGetAsTextSEVERE() throws Exception {
		testGetAsText(TraceLevel.SEVERE);		
	}
	
	@Test
	public void testGetValueSEVERE() throws Exception {
		testGetValue(TraceLevel.SEVERE);		
	}
	
	@Test
	public void testGetAsTextCONFIG() throws Exception {
		testGetAsText(TraceLevel.CONFIG);		
	}
	
	@Test
	public void testGetValueCONFIG() throws Exception {
		testGetValue(TraceLevel.CONFIG);	
	}
	
	@Test
	public void testGetValueInvalid() throws Exception {
		try {
			propertyEditor.setAsText("WARN");
			Assert.fail("editor allowed invalid string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}   

}

