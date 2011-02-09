/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

/**
 * Runtime info about an Sbb abstract class.
 * @author martins
 *
 */
public interface AbstractSbbClassInfo {

	/**
	 * 
	 * @param methodName
	 * @param invoke
	 */
	public void setInvokeInfo(String methodName, boolean invoke);
	
	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbActivate();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbCreate();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbExceptionThrown();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbLoad();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbPassivate();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbPostCreate();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbRemove();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbRolledBack();
	
	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSbbStore();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeSetSbbContext();

	/**
	 * 
	 * @return
	 */
	public boolean isInvokeUnsetSbbContext();

}
