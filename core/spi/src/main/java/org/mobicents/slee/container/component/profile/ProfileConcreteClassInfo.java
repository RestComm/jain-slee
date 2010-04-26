/**
 * 
 */
package org.mobicents.slee.container.component.profile;

/**
 * @author martins
 *
 */
public interface ProfileConcreteClassInfo {

	/**
	 * @param methodName
	 * @param invoke
	 */
	public void setInvokeInfo(String methodName, boolean invoke);
	
	/**
	 * 
	 * @return the invokeProfileActivate
	 */
	public boolean isInvokeProfileActivate();

	/**
	 * 
	 * @return the invokeProfileInitialize
	 */
	public boolean isInvokeProfileInitialize();

	/**
	 * 
	 * @return the invokeProfileVerify
	 */
	public boolean isInvokeProfileVerify();

	/**
	 * 
	 * @return the invokeProfileLoad
	 */
	public boolean isInvokeProfileLoad();

	/**
	 * 
	 * @return the invokeProfilePassivate
	 */
	public boolean isInvokeProfilePassivate();

	/**
	 * 
	 * @return the invokeProfilePostCreate
	 */
	public boolean isInvokeProfilePostCreate();

	/**
	 * 
	 * @return the invokeProfileRemove
	 */
	public boolean isInvokeProfileRemove();
	
	/**
	 * 
	 * @return the invokeProfileStore
	 */
	public boolean isInvokeProfileStore();

	/**
	 * 
	 * @return the invokeSetProfileContext
	 */
	public boolean isInvokeSetProfileContext();
	
	/**
	 * 
	 * @return the invokeUnsetProfileContext
	 */
	public boolean isInvokeUnsetProfileContext();

}
