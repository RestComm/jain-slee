/**
 * 
 */
package org.mobicents.slee.container.management;

import javax.naming.NamingException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.sbb.SbbObjectPool;

/**
 * @author martins
 * TODO redo interface
 */
public interface SbbManagement extends SleeContainerModule {

	/**
	 * Deploys an SBB. This generates the code to convert abstract to concrete
	 * class and registers the component in the component table and creates an
	 * object pool for the sbb id.
	 * 
	 * @param mobicentsSbbDescriptor
	 *            the descriptor of the sbb to install
	 * @throws Exception
	 */
	public void installSbb(SbbComponent sbbComponent) throws Exception;

	/**
	 * 
	 * @param sbbComponent
	 * @throws SystemException
	 * @throws Exception
	 * @throws NamingException
	 */
	public void uninstallSbb(final SbbComponent sbbComponent)
			throws SystemException, Exception, NamingException;
	
	/**
	 * @param serviceComponent
	 */
	public void serviceUninstall(ServiceComponent serviceComponent);

	/**
	 * @param serviceComponent
	 */
	public void serviceInstall(ServiceComponent serviceComponent);
	
	/**
	 * 
	 * @return
	 */
	public SbbObjectPool getObjectPool(ServiceID serviceID, SbbID sbbID);

}
