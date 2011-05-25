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

package org.rhq.plugins.jslee.jbossas5;

/**
 * @author Ian Springer
 */
public class ApplicationServerPluginConfigurationProperties {
	public static final String SERVER_NAME = "serverName";
	public static final String NAMING_URL = "namingURL";
	public static final String PRINCIPAL = "principal";
	public static final String CREDENTIALS = "credentials";
	public static final String HOME_DIR = "homeDir";
	public static final String SERVER_HOME_DIR = "serverHomeDir";
	public static final String SERVER_TMP_DIR = "serverTmpDir";
	public static final String JAVA_HOME = "javaHome";
	public static final String BIND_ADDRESS = "bindAddress";
	public static final String START_SCRIPT_CONFIG_PROP = "startScript";
	public static final String SHUTDOWN_SCRIPT_CONFIG_PROP = "shutdownScript";
	public static final String SHUTDOWN_MBEAN_CONFIG_PROP = "shutdownMBeanName";
	public static final String SHUTDOWN_MBEAN_OPERATION_CONFIG_PROP = "shutdownMBeanOperation";
	public static final String SHUTDOWN_METHOD_CONFIG_PROP = "shutdownMethod";
	public static final String SCRIPT_PREFIX_CONFIG_PROP = "scriptPrefix";
	public static final String AVAIL_CHECK_PERIOD_CONFIG_PROP = "availabilityCheckPeriod";

	private ApplicationServerPluginConfigurationProperties() {
	}
}
