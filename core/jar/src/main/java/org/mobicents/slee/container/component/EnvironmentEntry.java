/*
 * EnvironmentEntry.java
 * 
 * Created on Nov 4, 2004
 * 
 * Created by: M. Ranganathan
 *
 * The Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.container.component;

import java.io.Serializable;

/**
 * Environment Entry in the SBB Deployment Descriptor. Zero or more env-entry
 * elements. A description element.This is an optional informational element. o
 * An env-entry–name element. This element specifies the location within the
 * JNDI component environment to which the environment entry value will be
 * bound. o An env-entry-value element. This element specifies the value that
 * will be bound to the location specified by the enventry–name element. o An
 * env-entry–type element. This element specifies the type of the value
 * specified by the environment env-entry element.
 * 
 * @author M. Ranganathan
 * 
 */
public class EnvironmentEntry implements Serializable {

	private String description;

	private String name;

	private String value;

	private String type;

	// FIXME: how this can be achieved? for other packages than subpackage of
	// this package?
	// protected EnvironmentEntry (String description,
	// String name, String value, String type) {
	// this.description = description;
	// this.name = name;
	// this.value = value;
	// this.type = type;
	//    
	// }
	public EnvironmentEntry(String description, String name, String value,
			String type) {
		this.description = description;
		this.name = name;
		this.value = value;
		this.type = type;

	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	public String toString() {
		return "Description: " + this.description + " name: " + this.name
				+ " type: " + this.type + " value: " + this.value;
	}

}
