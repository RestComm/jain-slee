package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.library.JarDescriptor;

/**
 * 
 * MJar.java
 * 
 * <br>
 * Project: mobicents <br>
 * 3:34:29 AM Jan 30, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MJar implements JarDescriptor {

	private String description;

	private String jarName;

	private String securityPermissions;

	public MJar(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.library.Jar jar11) {
		this.description = jar11.getDescription() == null ? null : jar11
				.getDescription().getvalue();

		this.jarName = jar11.getJarName().getvalue();

		if (jar11.getSecurityPermissions() != null)
			this.securityPermissions = new MSecurityPermissions(jar11
					.getSecurityPermissions()).getSecurityPermissionSpec();
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.core.component.library.JarDescriptor#getJarName()
	 */
	public String getJarName() {
		return jarName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.library.JarDescriptor#
	 * getSecurityPermissions()
	 */
	public String getSecurityPermissions() {
		return securityPermissions;
	}

}
