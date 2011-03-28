
/**
 *   Copyright 2006 Alcatel, OSP.
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

import java.util.ArrayList;
import java.util.List;

/**
 *  Description:
 * <p>
 * Data object used to transport information about alarm refactoring.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AlarmRefactoringDO {
	/** The alarm for which all methods found ara called*/
	private Alarm alarm = null;
	/** The list of alarm method calls (createAlarm(..)*/
	private List alarmsMethodCall = null;
	/** The list of parameters of a method call each paramters set is a string of 5 or 6 paramters.*/
	private List  methodCallParameters = null;

	/**
	 * 
	 */
	public AlarmRefactoringDO() {
		this.alarmsMethodCall = new ArrayList();
		this.methodCallParameters = new ArrayList();
	}
	
	public void addMethodCall(String methodCall, String[] parameters){
		alarmsMethodCall.add(methodCall);
		methodCallParameters.add(parameters);
	}

	public List getAlarmsMethodCall() {
		return alarmsMethodCall;
	}

	public List getMethodCallParameters() {
		return methodCallParameters;
	}

	/**
	 * @return Returns the alarm.
	 */
	public Alarm getAlarm() {
		return alarm;
	}

	/**
	 * @param alarm The alarm to set.
	 */
	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

}
