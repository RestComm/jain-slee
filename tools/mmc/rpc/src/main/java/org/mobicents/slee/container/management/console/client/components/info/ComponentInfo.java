/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.container.management.console.client.components.info;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano Zappaterra
 *
 */
public abstract class ComponentInfo implements IsSerializable {
	
	final static public String EVENT_TYPE = "Event Type";
	final static public String PROFILE_SPECIFICATION = "Profile Specification";
	final static public String RESOURCE_ADAPTOR = "Resource Adaptor";
	final static public String RESOURCE_ADAPTOR_TYPE = "Resource Adaptor Type";
	final static public String SBB = "Sbb";
	final static public String SERVICE = "Service";
	
	protected String componentType = "Component";
	
	private String name;
	private String source;
	private String vendor;
	private String version;
	
	private String ID;
	private String deployableUnitID;
	
	public ComponentInfo() {
	}
	
	public ComponentInfo(String name, String source, String vendor, String version, String ID, String deployableUnitID) {
		super();
		this.name = name;
		this.source = source;
		this.vendor = vendor;
		this.version = version;
		this.ID = ID; 
		this.deployableUnitID = deployableUnitID;
	}

	public String getID() {
		return ID;
	}
	
	public String getDeployableUnitID() {
		return deployableUnitID;
	}	

	public String getName() {
		return name;
	}

	public String getSource() {
		return source;
	}

	public String getVendor() {
		return vendor;
	}

	public String getVersion() {
		return version;
	}
	
	public String getComponentType() {
		return componentType;
	}
}
