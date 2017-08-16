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

package org.mobicents.slee.container.component.deployment.classloading;

import javax.slee.ComponentID;

import org.mobicents.slee.container.component.classloading.ComponentClassLoader;

/**
 * The SLEE component class loader implementation.
 * 
 * A component needs to have it's own class loader due to unique JNDI context
 * but in reality it just delegates to the related domain class loader, which
 * manages the jar where the component was deployed from.
 * 
 * @author martins
 * 
 */
public class ComponentClassLoaderImpl extends ComponentClassLoader {

	/**
	 * the component id, used to make this class loader unique
	 */
	private final ComponentID componentID;
	
	/**
	 * the parent class loader domain
	 */
	private URLClassLoaderDomainImpl parent;
	
	/**
	 * 
	 * @param componentID
	 * @param parent
	 */
	public ComponentClassLoaderImpl(ComponentID componentID,
			URLClassLoaderDomainImpl parent) {
		super(parent);
		this.parent = parent;
		this.componentID = componentID;
	}

	/**
	 * Loads a class locally, i.e., from the component domain managed URLs.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClassLocally(String name) throws ClassNotFoundException {
		return parent.findClassLocally(name);	
	}
	
	@Override
	public String toString() {
		return "ComponentClassLoader[ componentID = " + componentID + " ]";
	}

}
