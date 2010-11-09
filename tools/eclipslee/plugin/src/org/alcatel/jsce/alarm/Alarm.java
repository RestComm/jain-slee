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
package org.alcatel.jsce.alarm;

/**
 *  Description:
 * <p>
 * This object represents an OSP Alarm event.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class Alarm {
	/** the name of the alarm*/
	private String name = null;
	/** Emmiter (not sure)*/
	private String problem = null;
	/** The alarm level*/
	private String level = null;
	/** The number of the alarm*/
	private int  alarmNumber = 0;
	/** The first line message*/
	private String type = null;
	/** Second line message*/
	private String effect = null;
	/** Third line message*/
	private String msg3 = null;
	/** The cause of the alarm*/
	private String cause = null;
	/** The action which should be taken*/
	private String action = null;
	/** The alarm catalog which contains this alarm*/
	private AlarmsCatalog catalog = null;

	/**
	 * Constructor;
	 */
	public Alarm() {
		this.name = "?";
		this.problem = "?";
		this.level = "minor";
		this.effect=this.msg3 = this.cause=this.action = "Not provided";
		this.type="0";
	}
	
	///////////////////////////////////////////
	//
	// Access
	//
	//////////////////////////////////////////

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return Returns the alarmNumber.
	 */
	public int getAlarmNumber() {
		return alarmNumber;
	}

	/**
	 * @param alarmNumber The alarmNumber to set.
	 */
	public void setAlarmNumber(int alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	/**
	 * @return Returns the cause.
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * @param cause The cause to set.
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * @return Returns the problem.
	 */
	public String getProblem() {
		return problem;
	}

	/**
	 * @param problem The problem to set.
	 */
	public void setProblem(String domain) {
		this.problem = domain;
	}

	/**
	 * @return Returns the level.
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level The level to set.
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * @return Returns the msg1.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param msg1 The msg1 to set.
	 */
	public void setType(String msg1) {
		this.type = msg1;
	}

	/**
	 * @return Returns the msg2.
	 */
	public String getEffect() {
		return effect;
	}

	/**
	 * @param msg2 The msg2 to set.
	 */
	public void setEffect(String msg2) {
		this.effect = msg2;
	}

	/**
	 * @return Returns the msg3.
	 */
	public String getMsg3() {
		return msg3;
	}

	/**
	 * @param msg3 The msg3 to set.
	 */
	public void setMsg3(String msg3) {
		this.msg3 = msg3;
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
	 * @return Returns the catalog.
	 */
	public AlarmsCatalog getCatalog() {
		return catalog;
	}

	/**
	 * @param catalog The catalog to set.
	 */
	public void setCatalog(AlarmsCatalog catalog) {
		this.catalog = catalog;
	}
	
}
