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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Start time:11:40:22 2009-01-31<br>
 * Project: restcomm-jainslee-server-core<br>
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
