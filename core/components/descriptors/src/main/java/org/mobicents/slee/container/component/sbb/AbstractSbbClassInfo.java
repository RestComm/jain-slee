/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import javax.slee.Sbb;

import org.apache.log4j.Logger;

/**
 * Information about the need to invoke {@link Sbb} methods.
 * 
 * @author martins
 * 
 */
public class AbstractSbbClassInfo {

	private static final Logger logger = Logger.getLogger(AbstractSbbClassInfo.class);
	
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
