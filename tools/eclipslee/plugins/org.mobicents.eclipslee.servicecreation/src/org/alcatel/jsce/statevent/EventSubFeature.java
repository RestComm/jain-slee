
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
 *  Describes the sub feature type of an stat. event.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class EventSubFeature {
	/** The parent category*/
	private String parent = null;
	/** The name of the type*/
	private String name = null;
	/** The init value */
	private int value = 0;
	/** List of @link EventType*/
	private List eventTypes = null;

	
	/**
	 * Constructor.
	 */
	public EventSubFeature() {
		eventTypes = new ArrayList();
	}

	///////////////////////////////////////////
	//
	// Access Methods
	//
	//////////////////////////////////////////

	/**
	 * @return Returns the eventTypes @link EventType
	 */
	public List getEventTypes() {
		return eventTypes;
	}


	/**
	 * @param eventTypes The eventTypes to set.
	 */
	public void setEventTypes(List events) {
		this.eventTypes = events;
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
