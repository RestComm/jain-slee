package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.ArrayList;
import java.util.List;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.Jar;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.du.ServiceXml;

public class DeployableUnitDescriptorImpl {

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
