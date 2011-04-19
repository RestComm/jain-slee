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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.profile.query.RangeMatchDescriptor;

/**
 * Start time:11:59:13 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
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
