/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import org.mobicents.slee.container.component.common.EnvEntryDescriptor;

/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEnvEntry implements EnvEntryDescriptor {

	private final String description, envEntryName, envEntryValue,
			envEntryType;

	public MEnvEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry) {
		this(envEntry.getDescription() == null ? null : envEntry
				.getDescription().getvalue().trim(), envEntry.getEnvEntryName()
				.getvalue().trim(), envEntry.getEnvEntryValue() == null ? null
				: envEntry.getEnvEntryValue().getvalue(), envEntry
				.getEnvEntryType().getvalue().trim());
	}

	public MEnvEntry(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EnvEntry envEntry) {
		this(envEntry.getDescription() == null ? null : envEntry
				.getDescription().getvalue().trim(), envEntry.getEnvEntryName()
				.getvalue().trim(), envEntry.getEnvEntryValue() == null ? null
				: envEntry.getEnvEntryValue().getvalue(), envEntry
				.getEnvEntryType().getvalue().trim());
	}

	public MEnvEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EnvEntry envEntry) {

		this(envEntry.getDescription() == null ? null : envEntry
				.getDescription().getvalue().trim(), envEntry.getEnvEntryName()
				.getvalue().trim(), envEntry.getEnvEntryValue() == null ? null
				: envEntry.getEnvEntryValue().getvalue(), envEntry
				.getEnvEntryType().getvalue().trim());
	}

	private MEnvEntry(String description, String envEntryName,
			String envEntryValue, String envEntryType) {
		this.description = description;
		this.envEntryName = envEntryName;
		this.envEntryValue = envEntryValue;
		this.envEntryType = envEntryType;
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
