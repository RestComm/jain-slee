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

import javax.slee.facilities.Level;

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.editors.LevelPropertyEditor;

/**
 * 
 * @author martins
 *
 */
public class LevelPropertyEditorTest {
	
	private LevelPropertyEditor propertyEditor = new LevelPropertyEditor();
	
	private void testGetAsText(Level level) throws Exception {
		propertyEditor.setValue(level);
		Assert.assertEquals(level.toString(), propertyEditor.getAsText());	
	}
	
	private void testGetValue(Level level) throws Exception {
		propertyEditor.setAsText(level.toString());
		Assert.assertEquals(level, propertyEditor.getValue());
	}
	
	@Test
	public void testGetAsTextFINE() throws Exception {
		testGetAsText(Level.FINE);	
	}
	
	@Test
	public void testGetValueFINE() throws Exception {
		testGetValue(Level.FINE);	
	}
	
	@Test
	public void testGetAsTextFINER() throws Exception {
		testGetAsText(Level.FINER);		
	}
	
	@Test
	public void testGetValueFINER() throws Exception {
		testGetValue(Level.FINER);			
	}
	
	@Test
	public void testGetAsTextOFF() throws Exception {
		testGetAsText(Level.OFF);		
	}
	
	@Test
	public void testGetValueOFF() throws Exception {
		testGetValue(Level.OFF);		
	}
	
	@Test
	public void testGetAsTextFINEST() throws Exception {
		testGetAsText(Level.FINEST);		
	}
	
	@Test
	public void testGetValueFINEST() throws Exception {
		testGetValue(Level.FINEST);		
	}
	
	@Test
	public void testGetAsTextINFO() throws Exception {
		testGetAsText(Level.INFO);		
	}
	
	@Test
	public void testGetValueINFO() throws Exception {
		testGetValue(Level.INFO);	
	}
	
	@Test
	public void testGetAsTextWARNING() throws Exception {
		testGetAsText(Level.WARNING);		
	}
	
	@Test
	public void testGetValueWARNING() throws Exception {
		testGetValue(Level.WARNING);		
	}
	
	@Test
	public void testGetAsTextSEVERE() throws Exception {
		testGetAsText(Level.SEVERE);		
	}
	
	@Test
	public void testGetValueSEVERE() throws Exception {
		testGetValue(Level.SEVERE);		
	}
	
	@Test
	public void testGetAsTextCONFIG() throws Exception {
		testGetAsText(Level.CONFIG);		
	}
	
	@Test
	public void testGetValueCONFIG() throws Exception {
		testGetValue(Level.CONFIG);	
	}
	
	@Test
	public void getValueInvalidTest() throws Exception {
		try {
			propertyEditor.setAsText("WARN");
			Assert.fail("editor allowed invalid string "+propertyEditor.getAsText());
		}
		catch (IllegalArgumentException e) {
			// expected
		}				
	}   

}

