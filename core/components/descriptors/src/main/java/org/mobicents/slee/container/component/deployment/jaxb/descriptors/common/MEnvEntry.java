/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EnvEntry;

/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEnvEntry {

	private String description, envEntryName, envEntryValue, envEntryType;
	private EnvEntry envEntry;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EnvEntry llEnvEntry;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry11;

	public MEnvEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry11) {
		this.envEntry11 = envEntry11;

		this.description = envEntry11.getDescription() == null ? null
				: envEntry11.getDescription().getvalue();

		this.envEntryName = envEntry11.getEnvEntryName().getvalue();
		this.envEntryType = envEntry11.getEnvEntryType().getvalue();
		this.envEntryValue = envEntry11.getEnvEntryValue() == null ? null
				: envEntry11.getEnvEntryValue().getvalue();
	}

	public MEnvEntry(EnvEntry envEntry) {
		super();
		this.envEntry = envEntry;
		this.description = this.envEntry.getDescription() == null ? null
				: this.envEntry.getDescription().getvalue();

		this.envEntryName = this.envEntry.getEnvEntryName().getvalue();
		// Optional
		if (this.envEntry.getEnvEntryValue() != null)
			this.envEntryValue = this.envEntry.getEnvEntryValue().getvalue();

		this.envEntryType = this.envEntry.getEnvEntryType().getvalue();
	}

	public MEnvEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EnvEntry envEntry) {
		super();
		this.llEnvEntry = envEntry;
		this.description = this.llEnvEntry.getDescription() == null ? null
				: this.llEnvEntry.getDescription().getvalue();

		this.envEntryName = this.llEnvEntry.getEnvEntryName().getvalue();
		// Optional
		if (this.llEnvEntry.getEnvEntryValue() != null)
			this.envEntryValue = this.llEnvEntry.getEnvEntryValue().getvalue();

		this.envEntryType = this.llEnvEntry.getEnvEntryType().getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getEnvEntryName() {
		return envEntryName;
	}

	public String getEnvEntryValue() {
		return envEntryValue;
	}

	public String getEnvEntryType() {
		return envEntryType;
	}
}
