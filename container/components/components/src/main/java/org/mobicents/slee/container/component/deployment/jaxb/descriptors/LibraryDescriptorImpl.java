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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.List;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.library.MLibrary;
import org.mobicents.slee.container.component.library.JarDescriptor;
import org.mobicents.slee.container.component.library.LibraryDescriptor;

/**
 * 
 * LibraryDescriptorImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:52:21 AM Jan 30, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LibraryDescriptorImpl extends AbstractComponentWithLibraryRefsDescriptor implements LibraryDescriptor {

  private final LibraryID libraryID;
  private final List<JarDescriptor> jars;
  private final String securityPermissions;
  
  public LibraryDescriptorImpl(MLibrary library, MSecurityPermissions mSecurityPermissions, boolean isSlee11) throws DeploymentException {
	  super(isSlee11);
	  super.setLibraryRefs(library.getLibraryRefs());
	  try {
		  this.jars = library.getJar();
		  this.securityPermissions = mSecurityPermissions == null ? null : mSecurityPermissions.getSecurityPermissionSpec();
		  this.libraryID = new LibraryID(library.getLibraryName(), library.getLibraryVendor(),library.getLibraryVersion());
		  // add other type deps
		  for (EventTypeID eventTypeID : library.getEventTypeRefs()) {
			  super.dependenciesSet.add(eventTypeID);
		  }
		  for (ProfileSpecificationID profileSpecificationID : library.getProfileSpecRefs()) {
			  super.dependenciesSet.add(profileSpecificationID);
		  }
		  for (ResourceAdaptorTypeID raTypeID : library.getRaTypeRefs()) {
			  super.dependenciesSet.add(raTypeID);
		  }
		  for (SbbID sbbID : library.getSbbRefs()) {
			  super.dependenciesSet.add(sbbID);
		  }
	  }
	  catch (Exception e) {
		  throw new DeploymentException("Failed to build library descriptor", e);      
	  }
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptor#getJars()
   */
  public List<JarDescriptor> getJars() {
    return jars;
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptor#getLibraryID()
   */
  public LibraryID getLibraryID() {
	return libraryID;
  }
  
  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.core.component.library.LibraryDescriptor#getSecurityPermissions()
   */
  public String getSecurityPermissions() {
    return this.securityPermissions;
  }
  
}
