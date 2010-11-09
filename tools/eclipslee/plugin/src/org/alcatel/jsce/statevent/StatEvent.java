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
package org.alcatel.jsce.statevent;

/**
 *  Description:
 * <p>
 * Represents an OSP Statistic event.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class StatEvent {
	/** Defines the parent of the event (typically the type + subfeature)*/
	private String parent = null;
	/** The name*/
	private String name = null;
	/** Initiale value*/
	private int value = 0;
	/** Defines the type of the event, the combination*/
	private String inc_type = null;
	/** Defines whether a dump is present or not*/
	private String dump_ind = null;
	/** the description of the event stat*/
	private String  description = null;
	/** The called macro*/
	private String macro = null;
	/** the treatment*/
	private String smp_inc_type = null;

	/**
	 * Constructror.
	 */
	public StatEvent() {
		parent="0.0.0";
		name= "not provided";
		inc_type="n";
		dump_ind = "n";
		description = "not provided";
		smp_inc_type="n";
		smp_inc_type="n";
		macro="none";
	}
	
	///////////////////////////////////////////
	//
	// Access methods
	//
	//////////////////////////////////////////

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description.replaceAll("\n", "").replaceAll("\r", "");
	}

	/**
	 * @return Returns the dump_ind.
	 */
	public String getDump_ind() {
		return dump_ind;
	}

	/**
	 * @param dump_ind The dump_ind to set.
	 */
	public void setDump_ind(String dump_ind) {
		this.dump_ind = dump_ind;
	}

	/**
	 * @return Returns the inc_type.
	 */
	public String getInc_type() {
		return inc_type;
	}

	/**
	 * @param inc_type The inc_type to set.
	 */
	public void setInc_type(String inc_type) {
		this.inc_type = inc_type;
	}

	/**
	 * @return Returns the macro.
	 */
	public String getMacro() {
		return macro;
	}

	/**
	 * @param macro The macro to set.
	 */
	public void setMacro(String macro) {
		this.macro = macro;
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

	/**
	 * @return Returns the parent.
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the smp_inc_type.
	 */
	public String getSmp_inc_type() {
		return smp_inc_type;
	}

	/**
	 * @param smp_inc_type The smp_inc_type to set.
	 */
	public void setSmp_inc_type(String smp_inc_type) {
		this.smp_inc_type = smp_inc_type;
	}

	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}

}
