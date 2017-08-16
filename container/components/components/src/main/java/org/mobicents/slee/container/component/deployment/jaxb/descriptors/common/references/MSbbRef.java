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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.SbbID;

import org.mobicents.slee.container.component.sbb.SbbRefDescriptor;

/**
 * Start time:10:25:19 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbRef implements SbbRefDescriptor {

	private String sbbAlias;

	private SbbID sbbID;

	public MSbbRef(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbRef sbbRef11) {
		String sbbName = sbbRef11.getSbbName().getvalue();
		String sbbVendor = sbbRef11.getSbbVendor().getvalue();
		String sbbVersion = sbbRef11.getSbbVersion().getvalue();

		this.sbbAlias = sbbRef11.getSbbAlias().getvalue();

		this.sbbID = new SbbID(sbbName, sbbVendor, sbbVersion);
	}

	public MSbbRef(
			org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbRef sbbRef10) {
		String sbbName = sbbRef10.getSbbName().getvalue();
		String sbbVendor = sbbRef10.getSbbVendor().getvalue();
		String sbbVersion = sbbRef10.getSbbVersion().getvalue();

		this.sbbAlias = sbbRef10.getSbbAlias().getvalue();

		this.sbbID = new SbbID(sbbName, sbbVendor, sbbVersion);
	}

	public String getSbbAlias() {
		return sbbAlias;
	}

	public SbbID getComponentID() {
		return this.sbbID;
	}

}
