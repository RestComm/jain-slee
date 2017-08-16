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
 * Start time:00:46:57 2009-02-04<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.io.File;
import java.net.URI;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.library.JarDescriptor;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.security.PermissionHolderImpl;

/**
 * Start time:00:46:57 2009-02-04<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryComponentImpl extends AbstractSleeComponent implements LibraryComponent {

	/**
	 * the library descriptor
	 */
	private final LibraryDescriptorImpl descriptor;

	/**
	 * the JAIN SLEE specs descriptor
	 */
	private javax.slee.management.LibraryDescriptor specsDescriptor;

	/**
	 * 
	 * @param descriptor
	 */
	public LibraryComponentImpl(LibraryDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the library descriptor
	 * 
	 * @return
	 */
	public LibraryDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the library id
	 * 
	 * @return
	 */
	public LibraryID getLibraryID() {
		return descriptor.getLibraryID();
	}

	@Override
	public boolean addToDeployableUnit() {
		return getDeployableUnit().getLibraryComponents().put(getLibraryID(), this) == null;
	}

	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}

	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}

	@Override
	public ComponentID getComponentID() {
		return getLibraryID();
	}

	@Override
	public boolean validate() throws DependencyException, DeploymentException {
		// nothing to validate ?
		return true;
	}

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.management.LibraryDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			final LibraryID[] libraryIDs = descriptor.getLibraryRefs().toArray(new LibraryID[descriptor.getLibraryRefs().size()]);
			final JarDescriptor[] jars = descriptor.getJars().toArray(new JarDescriptor[descriptor.getJars().size()]);
			String[] libraryJars = new String[jars.length];
			int i = 0;
			for(JarDescriptor jar : jars) {
			  libraryJars[i++] = jar.getJarName();
			}
			specsDescriptor = new javax.slee.management.LibraryDescriptor(getLibraryID(), getDeployableUnit().getDeployableUnitID(), getDeploymentUnitSource(), libraryIDs, libraryJars);
		}
		return specsDescriptor;
	}

	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor();
	}

	@Override
	public void processSecurityPermissions() throws DeploymentException {
		
		PermissionHolderImpl master = null;
		PermissionHolderImpl child = null;
		try {
			if (this.descriptor.getSecurityPermissions() != null) {
				
				URI deployURI =super.getDeploymentDir().toURI();
				
				//deployURI = new URI(deployURI.toString()+"-");
				
				master=new PermissionHolderImpl(deployURI, this.descriptor.getSecurityPermissions());
				super.permissions.add(master);
				//System.err.println("ADD MASTER: "+super.permissions.add(master));
			}

			for (JarDescriptor mjar : this.descriptor.getJars()) {
				if (mjar.getSecurityPermissions() != null) {
					File f = new File(super.getDeploymentDir(), mjar.getJarName());
					//System.err.println("PERMISSION FOR LIB1:"+Arrays.toString(super.permissions.toArray()));
					child = new PermissionHolderImpl(f.toURI(), mjar.getSecurityPermissions());
					super.permissions.add(child);
					//System.err.println("ADD: "+super.permissions.add(child));
					//System.err.println("PERMISSION FOR LIB2:"+Arrays.toString(super.permissions.toArray()));
				}
			}
	
		} catch (Exception e) {
			throw new DeploymentException("Failed to make permissions usable.", e);
		}
	}

	@Override
	public void undeployed() {
		super.undeployed();
		specsDescriptor = null;
	}

}
