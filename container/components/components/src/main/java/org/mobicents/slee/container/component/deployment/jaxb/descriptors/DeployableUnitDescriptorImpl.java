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

import java.util.ArrayList;
import java.util.List;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml;
import org.mobicents.slee.container.component.du.DeployableUnitDescriptor;

public class DeployableUnitDescriptorImpl implements DeployableUnitDescriptor {

	private final boolean isSlee11;
	
	private final List<String> jarEntries = new ArrayList<String>();
	private final List<String> serviceEntries = new ArrayList<String>();

	public DeployableUnitDescriptorImpl(org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit duDescriptor11)
	throws DeploymentException {		
		try {
			this.isSlee11 = true;
			buildDescriptionMap(duDescriptor11
					.getJarOrServiceXml());		
		} catch (DeploymentException e) {
			throw e;		
		} catch (Exception e) {
			throw new DeploymentException(
					"Failed to parse descriptor due to: ", e);
		}		
	}
	public DeployableUnitDescriptorImpl(org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit duDescriptor10)
			throws DeploymentException {
		try {
			this.isSlee11 = false;
			buildDescriptionMap(duDescriptor10.getJarOrServiceXml());		
		} catch (DeploymentException e) {
			throw e;		
		} catch (Exception e) {
			throw new DeploymentException(
					"Failed to parse descriptor due to: ", e);
		}
	}

	private void buildDescriptionMap(List<Object> jarOrServiceXml) throws DeploymentException {

		// This is akward, since we have two classes with the same name in
		// different package
		// We could use reflections but it would a killer in case of event
		// definitions and such ;[

			for (Object o : jarOrServiceXml) {
				if (o.getClass().getCanonicalName().contains("Jar")) {
					String v = null;
					if (this.isSlee11()) {
						Jar j = (Jar) o;
						v = j.getvalue();
					} else {
						org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar j = (org.mobicents.slee.container.component.deployment.jaxb.slee.du.Jar) o;
						v = j.getvalue();
					}

					this.jarEntries.add(v);

				} else if (o.getClass().getCanonicalName().contains("Service")) {
					String v = null;
					if (this.isSlee11()) {
						ServiceXml j = (ServiceXml) o;
						v = j.getvalue();
					} else {
						org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml j = (org.mobicents.slee.container.component.deployment.jaxb.slee.du.ServiceXml) o;
						v = j.getvalue();
					}
					this.serviceEntries.add(v);
				} else {
					throw new DeploymentException("Unknown jaxb du element: " + o.getClass());
				}
			}
	
	}

	public List<String> getJarEntries() {
		return jarEntries;
	}

	public List<String> getServiceEntries() {
		return serviceEntries;
	}

	public boolean isSlee11() {
		return isSlee11;
	}
}
