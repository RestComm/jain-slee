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

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano Zappaterra
 *
 */
public class ComponentSearchParams implements IsSerializable {
	
	private String name;
	
	private String id;
	
	private String vendor;
	
	private String version;

	public ComponentSearchParams() {
		super();
	}

	public ComponentSearchParams(String name, String id, String vendor, String version) throws ManagementConsoleException {
		super();
		this.name = name.trim();
		this.id = id.trim();
		this.vendor = vendor.trim();
		this.version = version.trim();
		validate();
	}
	
	public void validate() throws ManagementConsoleException {
		if (name != null && name.length() > 0 )
			return;
		if (id != null && id.length() > 0 )
			return;
		if (vendor != null && vendor.length() > 0 )
			return;
		if (version != null && version.length() > 0 )
			return;
		throw new ManagementConsoleException("No search parameter specified");
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVendor() {
		return vendor;
	}

	public String getVersion() {
		return version;
	}
	
	private boolean isTextDefined(String text) {
		if (text != null && text.length() > 0 )
			return true;
		return false;		
	}
	
	private boolean nameMatches(String name) {
		if(!isTextDefined(this.name))
			return true;
		
		if(!isTextDefined(name))
			return false;

		if(name.toLowerCase().indexOf(this.name.toLowerCase()) == -1)
			return false;
		
		return true;
	}

	private boolean idMatches(String id) {
		if(!isTextDefined(this.id))
			return true;
		
		if(!isTextDefined(id))
			return false;
		
		if(id.toLowerCase().indexOf(this.id.toLowerCase()) == -1)
			return false;
		
		return true;
	}

	private boolean vendorMatches(String vendor) {
		if(!isTextDefined(this.vendor))
			return true;
		
		if(!isTextDefined(vendor))
			return false;
		
		if(vendor.toLowerCase().indexOf(this.vendor.toLowerCase()) == -1)
			return false;
		
		return true;
	}

	private boolean versionMatches(String version) {
		if(!isTextDefined(this.version))
			return true;
		
		if(!isTextDefined(version))
			return false;
		
		if(version.toLowerCase().indexOf(this.version.toLowerCase()) == -1)
			return false;
		
		return true;
	}
	
	public boolean matches(String name, String id, String vendor, String version) {
		if (nameMatches(name) && idMatches(id) && vendorMatches(vendor) && versionMatches(version))
			return true;

		return false;
	}
}


