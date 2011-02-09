/**
 * 
 */
package org.mobicents.slee.container.sbb;

import javax.slee.ServiceID;

import org.mobicents.slee.container.component.sbb.SbbComponent;

/**
 * @author martins
 * 
 */
public interface SbbObjectPool {

	/**
	 * 
	 * @return
	 * @throws java.lang.Exception
	 * @throws java.util.NoSuchElementException
	 * @throws java.lang.IllegalStateException
	 */
	public SbbObject borrowObject() throws java.lang.Exception,
			java.util.NoSuchElementException, java.lang.IllegalStateException;

	/**
	 * @return the sbbComponent
	 */
	public SbbComponent getSbbComponent();

	/**
	 * @return the serviceID
	 */
	public ServiceID getServiceID();

	/**
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void invalidateObject(SbbObject obj) throws Exception;

	/**
	 * 
	 * @param obj
	 * @throws java.lang.Exception
	 */
	public void returnObject(SbbObject obj) throws java.lang.Exception;

}
