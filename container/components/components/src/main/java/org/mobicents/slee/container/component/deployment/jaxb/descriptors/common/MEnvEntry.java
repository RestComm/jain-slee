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
 * Start time:14:26:21 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import org.mobicents.slee.container.component.common.EnvEntryDescriptor;

/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: restcomm-jainslee-server-core<br>
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
