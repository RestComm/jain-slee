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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Start time:11:40:22 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MServiceXML {

	private final String description;
	private final String id;
	private final List<MService> mServices = new ArrayList<MService>();
	
	public MServiceXML(
			org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml serviceXML10) {
		this.description = serviceXML10.getDescription() == null ? null
				: serviceXML10.getDescription().getvalue();
		this.id = serviceXML10.getId();
		for (org.mobicents.slee.container.component.deployment.jaxb.slee.service.Service service : serviceXML10.getService()) {
			mServices.add(new MService(service));
		}
	}

	public MServiceXML(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml serviceXML11) {
		this.description = serviceXML11.getDescription() == null ? null
				: serviceXML11.getDescription().getvalue();
		this.id = serviceXML11.getId();
		for (org.mobicents.slee.container.component.deployment.jaxb.slee11.service.Service service : serviceXML11.getService()) {
			mServices.add(new MService(service));
		}
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}
	
	public List<MService> getMServices() {
		return mServices;
	}

}
