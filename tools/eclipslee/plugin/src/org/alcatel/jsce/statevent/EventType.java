
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

import java.util.ArrayList;
import java.util.List;


/**
 *  Description:
 * <p>
 *  Describe the type of an statistic event.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EventType {
	/** The parent category*/
	private String parent = null;
	/** The name of the type*/
	private String name = null;
	/** The init value */
	private int value = 0;
	/** TODO description*/
	private String rap_name = null;
	/** TODO descritpion*/
	private String rep_type = null;
	/** Defines the unit used*/
	private String unit = null;
	/** Desfines whether ?*/
	private boolean com_stat = false;
	/** The description of the type*/
	private String description = null;
	/**Represents the list of stat events.
	 *  List of @link StatEvent*/
	private List events = null;

	/**
	 * Constructor.
	 */
	public EventType() {
		parent = "1.0.0";
		description=rep_type=unit=rap_name=name="not provided";
		events = new ArrayList();
	}
	
	///////////////////////////////////////////
	//
	// Access methods
	//
	//////////////////////////////////////////
	

	/**
	 * @return Returns the com_stat.
	 */
	public boolean isCom_stat() {
		return com_stat;
	}

	/**
	 * @param com_stat The com_stat to set.
	 */
	public void setCom_stat(boolean com_stat) {
		this.com_stat = com_stat;
	}

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
		this.description = description;
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
	 * @return Returns the rap_name.
	 */
	public String getRap_name() {
		return rap_name;
	}

	/**
	 * @param rap_name The rap_name to set.
	 */
	public void setRap_name(String rap_name) {
		this.rap_name = rap_name;
	}

	/**
	 * @return Returns the rep_type.
	 */
	public String getRep_type() {
		return rep_type;
	}

	/**
	 * @param rep_type The rep_type to set.
	 */
	public void setRep_type(String rep_type) {
		this.rep_type = rep_type;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
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

	/**
	 * @return Returns the events.
	 */
	public List getEvents() {
		return events;
	}

	/**
	 * @param events The events to set.
	 */
	public void setEvents(List events) {
		this.events = events;
	}

}
