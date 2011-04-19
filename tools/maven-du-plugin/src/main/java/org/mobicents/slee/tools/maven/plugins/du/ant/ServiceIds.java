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

package org.mobicents.slee.tools.maven.plugins.du.ant;

import java.util.ArrayList;

import javax.slee.ServiceID;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ServiceIds {

	private final ArrayList<ServiceID> ids;
	
	public static ServiceIds parse(Element element) {
		ArrayList<ServiceID> ids = new ArrayList<ServiceID>();
		
		/*
		 <service-xml>
    		<service>
        		<service-name>SimpleCallSetupTerminatedByServerWithDialogsTestService</service-name>
        		<service-vendor>org.mobicents</service-vendor>
        		<service-version>1.0</service-version>
        		...
    		</service>
		</service-xml>
		 */
		NodeList nodeList = element.getElementsByTagName("service");
		for(int i=0;i<nodeList.getLength();i++) {
			Element service = (Element) nodeList.item(i);
			String name = ((Element)service.getElementsByTagName("service-name").item(0)).getTextContent();
			String vendor = ((Element)service.getElementsByTagName("service-vendor").item(0)).getTextContent();
			String version = ((Element)service.getElementsByTagName("service-version").item(0)).getTextContent();
			ids.add(new ServiceID(name, vendor, version));
		}
		return new ServiceIds(ids);
	}
	
	public ServiceIds(ArrayList<ServiceID> ids) {
		this.ids = ids;
	}
	
	public ArrayList<ServiceID> getIds() {
		return ids;
	}
}
