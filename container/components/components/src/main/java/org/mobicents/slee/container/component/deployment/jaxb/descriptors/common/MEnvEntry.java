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
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EnvEntryValue;

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
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("EnvEntryType").
			append("[name=").append(envEntryName).
			append(",value=").append(envEntryValue).
			append(']');
		return buf.toString();
	}
}
