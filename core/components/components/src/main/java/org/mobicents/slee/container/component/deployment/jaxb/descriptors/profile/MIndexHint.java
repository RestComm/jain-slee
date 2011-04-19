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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.profile.cmp.IndexHintDescriptor;

/**
 * Start time:16:51:40 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
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
