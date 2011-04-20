/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
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
package org.mobicents.eclipslee.servicecreation.util;

import java.util.HashMap;

import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorConfigPropertyXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ConfigPropertiesUtil {

	public static HashMap[] getConfigProperties(ResourceAdaptorXML sbb) {	
		ResourceAdaptorConfigPropertyXML[] entries = sbb.getConfigProperties();
		HashMap map[] = new HashMap[entries.length];
		
		for (int i = 0; i < entries.length; i++) {
			map[i] = new HashMap();
			map[i].put("Name", entries[i].getName() == null ? "" : entries[i].getName());
			map[i].put("Default Value", entries[i].getValue() == null ? "" : entries[i].getValue());
			map[i].put("Type", entries[i].getType() == null ? "" : entries[i].getType());
			map[i].put("Description", entries[i].getDescription() == null ? "" : entries[i].getDescription());
		}
		
		return map;		
	}
}
