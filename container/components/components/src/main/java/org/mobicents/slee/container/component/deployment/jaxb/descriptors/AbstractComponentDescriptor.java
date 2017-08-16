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
