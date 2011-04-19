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

/**
 * Start time:13:20:34 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;


import javax.slee.ComponentID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;


import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

/**
 * Start time:13:20:34 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class TCUtilityClass extends TestCase {

	protected EntityResolver resolver = null;
	protected DocumentBuilderFactory factory = null;
	protected DocumentBuilder builder = null;

	protected Document parseDocument(File f) throws SAXException, IOException {
		return builder.parse(f);
	}

	
	protected Document parseDocument(String name) throws SAXException, IOException, URISyntaxException {
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(
				name);
		return builder.parse(new File(url.toURI()));
	}
	@Override
	protected void setUp() throws Exception {

		super.setUp();
		this.resolver = new DefaultEntityResolver(Thread.currentThread()
				.getContextClassLoader());
		this.factory = DocumentBuilderFactory.newInstance();
		this.factory.setValidating(true);
		this.builder = this.factory.newDocumentBuilder();
		this.builder.setEntityResolver(this.resolver);
		
	}

	
	protected InputStream getFileStream(String filePath)
	{
		
		ClassLoader cl=Thread.currentThread().getContextClassLoader();
		try {
			return new FileInputStream( new File(cl.getResource(filePath).toURI()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		this.resolver = null;
		this.factory = null;
		this.builder = null;

	}
	
	
	
	protected void validateKey(ComponentID key, String text, String[] fieldValueAndName)
	{

		
		
		assertNotNull(text+" cant be null",key);
		assertNotNull(text+" "+fieldValueAndName[0]+" cant be null",key.getName());
		assertNotNull(text+" "+fieldValueAndName[1]+" cant be null",key.getVendor());
		assertNotNull(text+" "+fieldValueAndName[2]+" cant be null",key.getVersion());
		
	
		assertTrue(text+" "+fieldValueAndName[0]+" is not equal to "+key.getName(),key.getName().compareTo(fieldValueAndName[0])==0);
		assertTrue(text+" "+fieldValueAndName[1]+" is not equal to "+key.getVendor(),key.getVendor().compareTo(fieldValueAndName[1])==0);
		assertTrue(text+" "+fieldValueAndName[2]+" is not equal to "+key.getVersion(),key.getVersion().compareTo(fieldValueAndName[2])==0);
	}
	
	protected void validateValue(String value, String text, String presumableValue)
	{
		assertNotNull(text,value);
		assertTrue(text+": \""+value+"\" +is not equal to \""+presumableValue+"\"",value.compareTo(presumableValue)==0);
		
	}
	
}
