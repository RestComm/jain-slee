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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.profile.query.RangeMatchDescriptor;

/**
 * Start time:11:59:13 2009-01-29<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MRangeMatch implements RangeMatchDescriptor {

	private String attributeName;

	private String fromValue;
	private String fromParameter;
	
	private String toValue;
	private String toParameter;
	
	private String collatorRef;

	public MRangeMatch(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.RangeMatch rangeMatch11)
	{		
		this.attributeName = rangeMatch11.getAttributeName();
		
		this.fromValue = rangeMatch11.getFromValue();
		this.fromParameter = rangeMatch11.getFromParameter();
		
		this.toValue = rangeMatch11.getToValue();
		this.toParameter = rangeMatch11.getToParameter();
		
		this.collatorRef = rangeMatch11.getCollatorRef();
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getFromValue() {
		return fromValue;
	}

	public String getFromParameter() {
		return fromParameter;
	}

	public String getToValue() {
		return toValue;
	}

	public String getToParameter() {
		return toParameter;
	}

	public String getCollatorRef() {
		return collatorRef;
	}

}
