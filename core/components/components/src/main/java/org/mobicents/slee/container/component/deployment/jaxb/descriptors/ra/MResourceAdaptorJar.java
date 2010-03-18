package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;

/**
 * 
 * MResourceAdaptorJar.java
 * 
 * <br>
 * Project: mobicents <br>
 * 8:08:27 PM Jan 22, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorJar {

	protected String description;
	protected List<MResourceAdaptor> resourceAdaptor = new ArrayList<MResourceAdaptor>();
	protected MSecurityPermissions securityPermissions;

	public MResourceAdaptorJar(org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptorJar resourceAdaptorJar10) {
		this.description = resourceAdaptorJar10.getDescription() == null ? null : resourceAdaptorJar10.getDescription().getvalue();

		for (org.mobicents.slee.container.component.deployment.jaxb.slee.ra.ResourceAdaptor resourceAdaptor10 : resourceAdaptorJar10
				.getResourceAdaptor()) {
			this.resourceAdaptor.add(new MResourceAdaptor(resourceAdaptor10));
		}
	}

	public MResourceAdaptorJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorJar resourceAdaptorJar11) {
		this.description = resourceAdaptorJar11.getDescription() == null ? null : resourceAdaptorJar11.getDescription().getvalue();

		for (org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptor resourceAdaptor11 : resourceAdaptorJar11
				.getResourceAdaptor()) {
			this.resourceAdaptor.add(new MResourceAdaptor(resourceAdaptor11));
		}

		if (resourceAdaptorJar11.getSecurityPermissions() != null)
			this.securityPermissions = new MSecurityPermissions(resourceAdaptorJar11.getSecurityPermissions());
	}

	public String getDescription() {
		return description;
	}

	public List<MResourceAdaptor> getResourceAdaptor() {
		return resourceAdaptor;
	}

	public MSecurityPermissions getSecurityPermissions() {
		return securityPermissions;
	}

}
