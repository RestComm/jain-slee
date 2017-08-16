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
import java.util.List;
import java.util.Set;

import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;

/**
 * @author martins
 * 
 */
public abstract class AbstractComponentWithLibraryRefsDescriptor extends
		AbstractComponentDescriptor implements
		ComponentWithLibraryRefsDescriptor {

	/**
	 * @param slee11
	 */
	public AbstractComponentWithLibraryRefsDescriptor(boolean slee11) {
		super(slee11);
	}

	private Set<LibraryID> libraryRefs = new HashSet<LibraryID>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ComponentWithLibraryRefsDescriptor#
	 * getLibraryRefs()
	 */
	public Set<LibraryID> getLibraryRefs() {
		return libraryRefs;
	}

	protected void setLibraryRefs(List<LibraryID> list) {
		for (LibraryID libraryID : list) {
			libraryRefs.add(libraryID);
		}
		this.dependenciesSet.addAll(libraryRefs);
	}
}
