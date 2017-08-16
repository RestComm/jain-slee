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
 * Start time:08:53:04 2009-04-16<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.management.jmx;

/**
 * Start time:08:53:04 2009-04-16<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * 
 * Simple MBean to allow access to some info so we know whats going on inside and provide means of polite instalation of policy.
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public interface PolicyMBeanImplMBean {

	public static final String OBJECT_NAME = "org.mobicents.slee:name=PolicyManagementMBean";
	
	/**
	 * Returns String form of urls that point files loaded
	 * @return
	 */
	public String getPolicyFilesURL();
	/**
	 * Return Arrays.toString form of code source URLs loaded into policy
	 * @return
	 */
	public String getCodeSources();
	
	public boolean isUseMPolicy();
	
	public void setUseMPolicy(boolean useMPolicy);
	
}
