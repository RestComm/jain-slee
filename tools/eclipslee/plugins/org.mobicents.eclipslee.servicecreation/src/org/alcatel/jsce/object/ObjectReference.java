
/**
 *   Copyright 2005 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.object;

/**
 *  Description:
 * <p>
 * Defines the object information which is referenced by an OSP object attribute (which its type is 
 * Object_ref).
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectReference {
	/** The name of the object reference*/
	private String name = "not specified";
	/** The name of the attribute referenced*/
	private String attributeName = "not specified";
	/** The vendor of the referenced object*/
	private String vendor = null;
	/** The version of the referenced attribute*/
	private String version = null;
	/** The name of the orignal object which contains the external reference*/
	private String orignalObjectName = null;
	/** The name of the orignal object attribute*/
	private String originalAttribute = null;
	/** The location of the Jar referenced*/
	private String jarLocation = null;
	/** The location of the export jar referenced*/
	private String exportJarLocation = null;

	/**
	 * @param attribute the orginal attribute which reference this refernece
	 */
	public ObjectReference(String attribute) {
		originalAttribute = attribute;
	}

	
	///////////////////////////////////////////
	//
	// Accessors
	//
	//////////////////////////////////////////

	/**
	 * @return Returns the attributeName.
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName The attributeName to set.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}


	public String getExportJarLocation() {
		return exportJarLocation;
	}


	public void setExportJarLocation(String exportJarLocation) {
		this.exportJarLocation = exportJarLocation;
	}


	public String getJarLocation() {
		return jarLocation;
	}


	public void setJarLocation(String jarLocation) {
		this.jarLocation = jarLocation;
	}


	/**
	 * @return Returns the vendor.
	 */
	public String getVendor() {
		return vendor;
	}


	/**
	 * @param vendor The vendor to set.
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}


	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}


	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	

}
