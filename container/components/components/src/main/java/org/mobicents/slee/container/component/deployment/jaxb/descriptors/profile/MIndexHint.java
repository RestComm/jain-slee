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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.profile.cmp.IndexHintDescriptor;

/**
 * Start time:16:51:40 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MIndexHint implements IndexHintDescriptor {
	
	private final String queryOperator;
	private final String collatorRef;

	public MIndexHint(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.IndexHint indexHint11) {	  
		this.queryOperator = indexHint11.getQueryOperator();
		this.collatorRef = indexHint11.getCollatorRef();
	}

	public String getQueryOperator() {
		return queryOperator;
	}

	public String getCollatorRef() {
		return collatorRef;
	}
}
