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

package org.mobicents.slee.services.sip.proxy.mbean;

import org.mobicents.slee.services.sip.common.ProxyConfiguration;



public interface ProxyConfiguratorMBean extends ProxyConfiguration {
	/**
	 * If proxy supports more than sips and sip uri schemes this is the method to add them.
	 * @param schemeToAdd
	 */
	public void addSupportedURIScheme(String schemeToAdd);
	/**
	 * This method removes uri scheme from allowed set
	 * @param schemeToRemove
	 */
	public void removeSupportedURIScheme(String schemeToRemove);
	
	/**
	 * Adds local domain - domain which after addition is considered to be served by this proxy. After add operation proxy resolves all contacts localy, allow to register.
	 * @param localDomainToAdd
	 */
	public void addLocalDomain(String localDomainToAdd);
	/**
	 * Removes domain from local set.
	 * @param localDomainToRemove
	 */
	public void removeLocalDomain(String localDomainToRemove);
	
	/**
	 * Sets this sip host name, in case of multiple interfaces and names its mandatory.
	 * @param sipHostName
	 */
	public void setSipHostName(String sipHostName);

	/**
	 * Sets port on which proxy should work
	 * @param port
	 */
	public void setSipPort(int port);
	
	/**
	 * Sets set of allowed transports
	 * @param transport
	 */
	public void setSipTransports(String[] transport);

//	/**
//	 * Currently commented out code. Adds host through which messages must pass through after leaving this proxy
//	 * @param pos
//	 * @param host
//	 */
//    public void addMustPassThrough(int pos,String host);
//    public void removeMustPassThrough(int pos);
//    public void removeMustPassThrough(String host);
    
    public Object clone();
}
