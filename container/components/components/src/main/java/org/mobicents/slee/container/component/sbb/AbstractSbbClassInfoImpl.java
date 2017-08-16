/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import javax.slee.Sbb;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.sbb.AbstractSbbClassInfo;

/**
 * Information about the need to invoke {@link Sbb} methods.
 * 
 * @author martins
 * 
 */
public class AbstractSbbClassInfoImpl implements AbstractSbbClassInfo {

	private static final Logger logger = Logger.getLogger(AbstractSbbClassInfoImpl.class);
	
	private boolean invokeSbbActivate;
	private boolean invokeSbbCreate;
	private boolean invokeSbbExceptionThrown;
	private boolean invokeSbbLoad;
	private boolean invokeSbbPassivate;
	private boolean invokeSbbPostCreate;
	private boolean invokeSbbRemove;
	private boolean invokeSbbRolledBack;
	private boolean invokeSbbStore;
	private boolean invokeSetSbbContext;
	private boolean invokeUnsetSbbContext;

	/**
	 * @param methodName
	 * @param invoke
	 */
	public void setInvokeInfo(String methodName, boolean invoke) {
		if (methodName.equals("sbbActivate")) {
			invokeSbbActivate = invoke;
		}
		else if (methodName.equals("sbbCreate")) {
			invokeSbbCreate = invoke;
		} 
		else if (methodName.equals("sbbExceptionThrown")) {
			invokeSbbExceptionThrown = invoke;
		}
		else if (methodName.equals("sbbLoad")) {
			invokeSbbLoad = invoke;
		}
		else if (methodName.equals("sbbPassivate")) {
			invokeSbbPassivate = invoke;
		}
		else if (methodName.equals("sbbPostCreate")) {
			invokeSbbPostCreate = invoke;
		}
		else if (methodName.equals("sbbRemove")) {
			invokeSbbRemove = invoke;
		}
		else if (methodName.equals("sbbRolledBack")) {
			invokeSbbRolledBack = invoke;
		}
		else if (methodName.equals("sbbStore")) {
			invokeSbbStore = invoke;
		}
		else if (methodName.equals("setSbbContext")) {
			invokeSetSbbContext = invoke;
		}
		else if (methodName.equals("unsetSbbContext")) {
			invokeUnsetSbbContext = invoke;
		}
		else {
			logger.warn("Unrecognized method from javax.slee.Sbb, unable to set info on whether to invoke it or not in runtime. Method name: "+methodName);
		}
	}
	
	/**
	 * 
	 * @return the invokeSbbActivate
	 */
	public boolean isInvokeSbbActivate() {
		return invokeSbbActivate;
	}

	/**
	 * 
	 * @return the invokeSbbCreate
	 */
	public boolean isInvokeSbbCreate() {
		return invokeSbbCreate;
	}

	/**
	 * 
	 * @return the invokeSbbExceptionThrown
	 */
	public boolean isInvokeSbbExceptionThrown() {
		return invokeSbbExceptionThrown;
	}

	/**
	 * 
	 * @return the invokeSbbLoad
	 */
	public boolean isInvokeSbbLoad() {
		return invokeSbbLoad;
	}

	/**
	 * 
	 * @return the invokeSbbPassivate
	 */
	public boolean isInvokeSbbPassivate() {
		return invokeSbbPassivate;
	}

	/**
	 * 
	 * @return the invokeSbbPostCreate
	 */
	public boolean isInvokeSbbPostCreate() {
		return invokeSbbPostCreate;
	}

	/**
	 * 
	 * @return the invokeSbbRemove
	 */
	public boolean isInvokeSbbRemove() {
		return invokeSbbRemove;
	}

	/**
	 *  
	 * @return the invokeSbbRolledBack
	 */
	public boolean isInvokeSbbRolledBack() {
		return invokeSbbRolledBack;
	}
	
	/**
	 * 
	 * @return the invokeSbbStore
	 */
	public boolean isInvokeSbbStore() {
		return invokeSbbStore;
	}

	/**
	 * 
	 * @return the invokeSetSbbContext
	 */
	public boolean isInvokeSetSbbContext() {
		return invokeSetSbbContext;
	}

	/**
	 * 
	 * @return the invokeUnsetSbbContext
	 */
	public boolean isInvokeUnsetSbbContext() {
		return invokeUnsetSbbContext;
	}

}
