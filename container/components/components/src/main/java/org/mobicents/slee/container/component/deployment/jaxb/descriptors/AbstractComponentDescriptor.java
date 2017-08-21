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
 * 
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;

import org.mobicents.slee.container.component.ComponentDescriptor;

/**
 * @author martins
 *
 */
public abstract class AbstractComponentDescriptor implements ComponentDescriptor {

	protected Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();
	
	private final boolean slee11;
	
	/**
	 * 
	 */
	public AbstractComponentDescriptor(boolean slee11) {
		this.slee11 = slee11;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentDescriptor#getDependenciesSet()
	 */
	public Set<ComponentID> getDependenciesSet() {
		return dependenciesSet;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentDescriptor#isSlee11()
	 */
	public boolean isSlee11() {
		return slee11;
	}
}
