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

package org.mobicents.slee.services.sip.common;

public interface ProxyConfiguration {

	public static final String MBEAN_NAME_PREFIX="slee:sipproxyconfigurator=";
	/**
	 * List of supported uri schemes by this proxy/ accepted uri schemes
	 * @return
	 */
	public String[] getSupportedURISchemes();
	/**
	 * List of domains for which this proxy should act.
	 * @return
	 */
	public String[] getLocalDomainNames();
	/**
     * This is only called to fill in the via header.
     * 
     * @return the sipHostName by local host lookup or by consulting
     * the load balancer.
     * 
     */
    public String getSipHostname();
    /**
     * Port for communication
     * @return
     */
    public int getSipPort();
    
    /**
     * Transport used by this proxy
     * @return
     */
    public String[] getSipTransports();

    

}
